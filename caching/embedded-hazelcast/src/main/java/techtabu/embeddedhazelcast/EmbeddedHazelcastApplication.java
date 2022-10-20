package techtabu.embeddedhazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmbeddedHazelcastApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmbeddedHazelcastApplication.class, args);
    }

}
