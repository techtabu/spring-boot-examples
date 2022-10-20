package techtabu.embeddedhazelcast;

import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author TechTabu
 */

@Component
@Slf4j
public class HazelcastClusterState {

    private final HazelcastInstance hazelcastInstance;

    public HazelcastClusterState(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    public void logClusterState() {
        log.info("\n\t ******* Hazelcast cluster info *****\n\t\tInstance: {}\n" +
                "\t\tCluster Members: {}\n\t\tState: {}",
                hazelcastInstance, hazelcastInstance.getCluster().getMembers(), hazelcastInstance.getCluster().getClusterState());
    }
}
