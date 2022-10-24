package techtabu.embeddedhazelcast;

import com.hazelcast.client.Client;
import com.hazelcast.client.ClientListener;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.config.Config;
import com.hazelcast.config.ListenerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


/**
 * @author TechTabu
 */

@Configuration
@Slf4j
public class HazelcastConfig {

    @Value("${cache.cluster-name:sbe-cache}")
    private String clusterName;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${cache.kubernetes.discovery.method}")
    private String discoveryMethod;

    @Value("${cache.kubernetes.namespace:default}")
    private String kubNamespace;

    @Value("${cache.kubernetes.service-dns}")
    private String serviceDns;

    @Value("${cache.kubernetes.service-name}")
    private String serviceName;

    @Value("${cache.diagnostic.enabled:true}")
    private boolean diagnosticEnabled;

    @Bean
    public Config hazelcastConfiguration() {
        Config config = new Config();

        config.setClusterName(clusterName);
        config.setInstanceName(appName + "_" + UUID.randomUUID());

        config.getNetworkConfig().getJoin().getKubernetesConfig()
                .setEnabled(true);

        if ("DNS_LOOKUP".equals(discoveryMethod)) {
            log.info("configuring Kubernetes with DNS_LOOKUP, service-dns: {}, namespace: {}", serviceDns, kubNamespace);
            config.getNetworkConfig().getJoin().getKubernetesConfig()
                    .setProperty("service-dns", serviceDns);
        } else if ("KUBERNETES_API".equals(discoveryMethod)) {
            log.info("configuring Kubernetes with KUBERNETES_API: {}, namespace: {}", serviceDns, kubNamespace);
            config.getNetworkConfig().getJoin().getKubernetesConfig()
                    .setProperty("namespace", kubNamespace)
                    .setProperty("service-name", serviceName);
        } else {
            log.error("unidentified discovery method");
        }

        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        if (diagnosticEnabled) {
            config.setProperty("hazelcast.diagnostics.enabled", "true");
            config.setProperty("hazelcast.diagnostics.metric.level", "info");
            config.setProperty("hazelcast.diagnostics.invocation.sample.period.seconds", "30");
            config.setProperty("hazelcast.diagnostics.pending.invocations.period.seconds", "30");
            config.setProperty("hazelcast.diagnostics.slowoperations.period.seconds", "30");
            config.setProperty("hazelcast.diagnostics.storeLatency.period.seconds", "60");
        }

        config.addListenerConfig(getListenerConfigs());
        config.addListenerConfig(new ListenerConfig(new ClusterMemberShipListener()));
        return config;
    }

    protected ListenerConfig getListenerConfigs() {

        ClientListener clientListener = clientListener();
        return new ListenerConfig(clientListener);
    }

    protected ClientListener clientListener() {
        return new ClientListener() {
            @Override
            public void clientConnected(Client client) {
                String type = client.getClientType();
                String uuid = client.getUuid().toString();
                String name = client.getName();
                String address = client.getSocketAddress().toString();
                log.info("Client {} of type {} connected uuid: {}, Address: {}", name, type, uuid, address);
            }

            @Override
            public void clientDisconnected(Client client) {
                String type = client.getClientType();
                String uuid = client.getUuid().toString();
                String name = client.getName();
                String address = client.getSocketAddress().toString();
                log.info("Client {} of type {} disconnected uuid: {}, Address: {}", name, type, uuid, address);
            }
        };
    }


}

@Slf4j
class ClusterMemberShipListener implements MembershipListener {

    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        String uuid = membershipEvent.getMember().getUuid().toString();
        String address = membershipEvent.getMember().getAddress().toString();
        log.info("Member with uuid: {} was added, address: {}", uuid, address);
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        String uuid = membershipEvent.getMember().getUuid().toString();
        String address = membershipEvent.getMember().getAddress().toString();
        log.info("Member with uuid: {} was removed, address: {}", uuid, address);
    }
}

