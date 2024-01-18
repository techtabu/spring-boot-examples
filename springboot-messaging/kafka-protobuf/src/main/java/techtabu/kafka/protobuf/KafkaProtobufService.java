package techtabu.kafka.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import techtabu.kafka.protobuf.customer.CustomerMessage.Customer;

/**
 * @author TechTabu
 */

@Service
@Slf4j
@EnableScheduling
public class KafkaProtobufService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProtobufService(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }



    @KafkaListener(topics = "protobuf-topic",
            groupId = "protobuf-topic-consumer-group",
            containerFactory = "kafkaListenerFactory")
    public void consumeMessages(byte[] message) throws InvalidProtocolBufferException {

        Customer customer = Customer.parseFrom(message);

        log.info("customer is: {}", customer.toString());

        techtabu.kafka.protobuf.customer.Customer cust = new techtabu.kafka.protobuf.customer.Customer(customer);
        log.info("Customer by model: {}", cust);

    }

    @Scheduled(fixedDelay = 5000)
    public void sendMessage() {
        Customer customer = Customer.newBuilder()
                .setFirstName("Nebula")
                .setLastName("Thanos")
                .setEmail("n.thanos@universe.com")
                .build();

        log.info("sending message for customer: {}", customer.toByteString());

        kafkaTemplate.send("protobuf-topic", customer.toByteArray());
    }
}
