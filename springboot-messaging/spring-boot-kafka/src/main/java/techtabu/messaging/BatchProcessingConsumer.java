package techtabu.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tabuthevarajan
 */

@Component
@Slf4j
public class BatchProcessingConsumer {


    @KafkaListener(id = "${spring.application.name}-batch-topics",
            topics = "batch-topic",
            groupId = "{spring.application.name}-batch-topic-group",
            containerFactory = "batchProcessingListenerFactory"
    )
    public  void batchConsumer(List<ConsumerRecord<String, String>> consumerRecord) {
//        log.info("Received value: {}", consumerRecord.value());
        log.info("received {} of consumer records", consumerRecord.size());
        consumerRecord.stream().forEach(c -> log.info("Received message: {}", c.value()));
    }
}
