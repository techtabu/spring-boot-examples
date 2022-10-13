package techtabu.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author TechTabu
 */
@Service
@Slf4j
public class KafkaSslService {

    @Value("${techtabu.messaging.topic:kafka-ssl-topic}")
    private String topic;

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaSslService(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "CSG-MASIMO-DEVICE", containerFactory = "kafkaListenerFactory")
    public void consumerKafka(String message) {
        log.info("message consumed: {}", message);
    }

    @KafkaListener(topics = "${techtabu.messaging.topic:kafka-ssl-topic}", containerFactory = "kafkaListenerFactory")
    public void consumerLocalKafka(String message) {
        log.info("message consumed in {}: {}", topic, message);
    }

    public void send(String message) {
        kafkaTemplate.send(topic, message.getBytes());
    }
}
