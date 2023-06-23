package techtabu.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.FixedBackOff;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TechTabu
 */

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic topicByBoot() {
        return TopicBuilder
                .name("topic_By_boot")
                .build();
    }

    @Bean("kafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerFactory.setConsumerFactory(consumerFactory);
        return listenerFactory;
    }

    @Bean("errorHandlingListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> errorHandlingListenerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(2);
        backOff.setInitialInterval(2000L);
        backOff.setMultiplier(2);
        DefaultErrorHandler handler = new DefaultErrorHandler(backOff);

//        handler.setBackOffFunction((c, b) -> new FixedBackOff(1000L, 2L));
//        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(2000L, 2L));
        handler.addNotRetryableExceptions(IllegalArgumentException.class);
        factory.setCommonErrorHandler(handler);

        return factory;
    }

    @Bean("batchProcessingListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> batchProcessingListenerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        consumerFactory.updateConfigs(props);
        factory.setBatchListener(true);
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean("kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) throws IOException {
        return new KafkaTemplate<>(producerFactory);
    }
}
