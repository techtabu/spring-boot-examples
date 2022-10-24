package techtabu.embeddedhazelcast;

import com.hazelcast.cluster.ClusterState;
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
    private ClusterState clusterState;

    public HazelcastClusterState(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    public void checkClusterState() {
        ClusterState newClusterState = hazelcastInstance.getCluster().getClusterState();
        if (newClusterState != clusterState) {
            clusterState = newClusterState;
            log.info("""

                            \t ******* Hazelcast cluster state changed *****
                            \t\tInstance: {}
                            \t\tCluster Members: {}
                            \t\tState: {}
                      """,
                    hazelcastInstance, hazelcastInstance.getCluster().getMembers(), clusterState);
        }

    }
}
