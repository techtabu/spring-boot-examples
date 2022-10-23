package techtabu.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author TechTabu
 */

@Configuration
@EnableCaching
@Slf4j
public class HazelcastConfig {

    @Value("${cache.client.cluster-name:sbe-cache}")
    private String clusterName;

    @Value("${cache.client.hosts:127.0.0.1}")
    protected List<String> hosts;

    @Value("${cache.client.connection-timeout:60000}")
    protected int clientConnectionTimeout = 60000;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${cache.kubernetes.enabled:true}")
    private boolean kubEnabled;

    @Value("${cache.kubernetes.namespace:default}")
    private String kubNamespace;

    @Value("${cache.kubernetes.service-name:default}")
    private String serviceName;

    @Value("${spring.config.name}")
    private String profile;

    @Bean
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(createHazelcastInstance());
    }

    protected HazelcastInstance createHazelcastInstance() {

        ClientConfig clientConfig = getHazelcastConfig();
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    protected ClientConfig getHazelcastConfig() {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClassLoader(Thread.currentThread().getContextClassLoader());
        clientConfig.setInstanceName(appName);
        clientConfig.setClusterName(clusterName);

        // Network Config
        clientConfig.getNetworkConfig()
                .setConnectionTimeout(clientConnectionTimeout)
                .setSmartRouting(true);

        if (kubEnabled) {
            log.info("Configuring Kubernetes client wth namespace: {} and service name: {}", kubNamespace, serviceName);
            clientConfig.getNetworkConfig().getKubernetesConfig()
                    .setEnabled(true)
                    .setProperty("namespace", kubNamespace)
                    .setProperty("service-name", serviceName);

        } else {
            log.info("Configuring non-kubernetes client with host: {}", hosts);
            clientConfig.getNetworkConfig().setAddresses(hosts);
        }

        return clientConfig;
    }
}
