package techtabu.messaging;

import lombok.extern.slf4j.Slf4j;
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

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
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

        throw new IllegalArgumentException("Testing retrying error handler");
    }

    @Scheduled(fixedDelay = 60000)
    public void sendMessage() {
//        log.info("sending message to topic by boot");
//        kafkaTemplate.send("topic_By_boot", Instant.now().toString());
    }

    public void send(String message) {
        kafkaTemplate.send("tabu-playground", message);
    }
}
