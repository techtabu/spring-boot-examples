package techtabu.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author TechTabu
 */
@Component("consumerAwareListenerErrorHandler")
@Slf4j
public class MyErrorHandler implements ConsumerAwareListenerErrorHandler {

    private  final KafkaTemplate<String, String> kafkaTemplate;

    public MyErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {

        log.info("\nMessage: {}\n\n", message);
        log.info("Exception Message: {} \n\n", exception.getMessage());
        log.info("Consumer: {} \n\n", consumer.groupMetadata());

        kafkaTemplate.send("dead-letter-topic", message.getPayload().toString());
        return null;
    }
}
