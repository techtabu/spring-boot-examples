package techtabu.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.IOException;

/**
 * @author TechTabu
 */

@Configuration
@EnableKafka
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public NewTopic topicByBoot() {
        return TopicBuilder.name("topic_By_boot").build();
    }

    @Bean("kafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerFactory.setConsumerFactory(consumerFactory);
        return listenerFactory;
    }

//    @Bean
//    public KafkaAdmin kafkaAdmin(KafkaAdmin kafkaAdmin) {
//        return new KafkaAdmin(kafkaAdmin.getConfigurationProperties());
//    }

    @Bean("kafkaTemplate")
    public KafkaTemplate<String, byte[]> kafkaTemplate(ProducerFactory<String, byte[]> producerFactory) throws IOException {
        return new KafkaTemplate<>(producerFactory);
    }
}
