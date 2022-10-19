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

    @Value("${cache.kubernetes.namespace:default}")
    private String kubNamespace;

    @Value("{spring.config.name}")
    private String profile;

    @Bean
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(createHazelcastInstance());
    }

    protected HazelcastInstance createHazelcastInstance() {
        ClientConfig clientConfig = getHazelcastConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        if ("kubernetes".equals(profile)) {
            client.getConfig().getNetworkConfig().getJoin().getKubernetesConfig()
                    .setEnabled(true)
                    .setProperty("namespace", kubNamespace)
                    .setProperty("service-name", "sbe-hazelcast");

            client.getConfig().getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        }

        return client;
    }

    protected ClientConfig getHazelcastConfig() {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClassLoader(Thread.currentThread().getContextClassLoader());
        clientConfig.setInstanceName(appName);
        clientConfig.setClusterName(clusterName);

        // Network Config
        log.info("Hosts: {}", hosts);
        clientConfig.getNetworkConfig()
                .setAddresses(hosts)
                .setConnectionTimeout(clientConnectionTimeout)
                .setSmartRouting(true);

        return clientConfig;
    }
}
