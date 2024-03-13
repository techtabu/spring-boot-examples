package techtabu.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TechTabu
 */

@Service
@EnableScheduling
@Slf4j
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, byte[]> kafkaByteTemplate;

    public KafkaService(@Qualifier("kafkaTemplate") KafkaTemplate<String, String> kafkaTemplate,
                        @Qualifier("kafkaByteTemplate") KafkaTemplate<String, byte[]> kafkaByteTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaByteTemplate = kafkaByteTemplate;
    }


    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(delay = 3000, multiplier = 2.0),
            retryTopicSuffix = "-${spring.application.name}-retry",
            dltTopicSuffix = "-${spring.application.name}-dlt",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            exclude = IllegalArgumentException.class
    )
    @KafkaListener(topics = "tabu-playground",
            groupId = "sb-playground",
//            errorHandler = "consumerAwareListenerErrorHandler",
            containerFactory = "kafkaListenerFactory")
    public void consumerKafka(String message,
                              @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics) {
        log.info("message consumed: {} from topics: {}", message, String.join(", ", topics));
        if (!StringUtils.hasText(message)) {
            throw new IllegalArgumentException("Oh no, empty string");
        }

        if ("Harry".equals(message)) {
            throw new IllegalCallerException("Oh Harry, you are now allowed to call");
        }
    }


    @KafkaListener(topics = "topic_By_boot",
                    groupId = "sb-kafka-topic-by-boot",
                    containerFactory = "errorHandlingListenerFactory")
    public void consumeAnotherTopic(String message) {
        log.info("message from topic by boot: {}", message);

        if ("Harry".equals(message)) {
            throw new IllegalCallerException("Oh Harry, you are now allowed to call");
        }

//        throw new IllegalArgumentException("Testing retrying error handler");
    }

    @Scheduled(fixedDelay = 10000)
    public void sendMessage() {
        log.info("sending string message to topic by boot");
        kafkaTemplate.send("topic_By_boot", "Sting time: " + Instant.now().toString());
    }

    public void send(String message) {
        kafkaTemplate.send("tabu-playground", message);
    }

    @Scheduled(fixedDelay = 15000)
    public void sendByteMessage() {
        log.info("sending byte message to topic by boot");
        kafkaByteTemplate.send("topic_By_boot", ("Byte time: " + Instant.now().toString()).getBytes(StandardCharsets.UTF_8));
    }
}
