package techtabu.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author TechTabu
 */

@Service
@EnableScheduling
@Slf4j
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // this itself is enough to consume, you don't have to define / declare the bean, and use it here.
    // but whatever, you can use it..
//    @KafkaListener(topics = "tabu-playground", groupId = "kafka-group")
//    public void consumerKafka(String message) {
//        log.info("message consumed: {}", message);
//    }

    @KafkaListener(topics = "tabu-playground", containerFactory = "kafkaListenerFactory")
    public void consumerKafka(String message) {
        log.info("message consumed: {}", message);
    }

    @KafkaListener(topics = "topic_By_boot")
    public void consumeAnotherTopic(String message) {
        log.info("message from topic by boot: {}", message);
    }

    @Scheduled(fixedDelay = 5000)
    public void sendMessage() {
        kafkaTemplate.send("topic_By_boot", Instant.now().toString());
    }

    public void send(String message) {
        kafkaTemplate.send("tabu-playground", message);
    }
}
