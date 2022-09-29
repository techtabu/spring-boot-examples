package techtabu.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author TechTabu
 */
@Service
@Slf4j
public class KafkaSslService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaSslService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "CSG-MASIMO-DEVICE", containerFactory = "kafkaListenerFactory")
    public void consumerKafka(String message) {
        log.info("message consumed: {}", message);
    }
}
