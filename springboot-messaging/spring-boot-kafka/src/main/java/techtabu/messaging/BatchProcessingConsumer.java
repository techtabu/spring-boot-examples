package techtabu.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author tabuthevarajan
 */

@Component
@Slf4j
public class BatchProcessingConsumer {

    Long startTime = 0L;


    @KafkaListener(id = "${spring.application.name}-batch-topics",
            topics = "batch-topic",
            groupId = "{spring.application.name}-batch-topic-group",
            containerFactory = "batchProcessingListenerFactory"
    )
    public  void batchConsumer(List<ConsumerRecord<String, String>> consumerRecord) {

//        log.info("Received value: {}", consumerRecord.value());
        log.info("received {} of consumer records", consumerRecord.size());
        consumerRecord.forEach(c -> {
            for (Header header : c.headers()) {
                log.info("Key: {}, Value: {}", header.key(), header.value());
            }
            Header header = c.headers().lastHeader("message_number");
            if (header.value() != null) {
                String val = new String(header.value());
                log.info("Count: {}, value: {}", header.value(), val);
                if (Arrays.toString(header.value()).equals("0")) {
                    startTime = Instant.now().toEpochMilli();
                } else if (Integer.parseInt(Arrays.toString(header.value())) % 10 == 0) {
                    Long now = Instant.now().toEpochMilli();
                    log.info("Time to to handle {} records {}", Integer.parseInt(Arrays.toString(header.value())) / 1000, now - startTime);
                }

            }
//            log.info("Received message: {}", c.value());
        });
    }
}
