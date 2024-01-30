package techtabu.kafka.protobuf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import techtabu.kafka.protobuf.customer.CustomerMessage.Customer;
import techtabu.kafka.protobuf.customer.CustomerRepository;

import java.nio.charset.StandardCharsets;

/**
 * @author TechTabu
 */

@Service
@Slf4j
@EnableScheduling
public class KafkaProtobufService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final CustomerRepository customerRepository;

    public KafkaProtobufService(KafkaTemplate<String, byte[]> kafkaTemplate,
                                CustomerRepository customerRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.customerRepository = customerRepository;
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

    @KafkaListener(topics = "protobuf-topic-cr",
            groupId = "protobuf-topic-consumer-record",
            containerFactory = "kafkaListenerFactory")
    public void consumeMessagesViaConsumerRecord(ConsumerRecord<String, byte[]> consumerRecord) throws InvalidProtocolBufferException, JsonProcessingException {

        Customer customer = Customer.parseFrom(consumerRecord.value());
        for (Header header : consumerRecord.headers()) {
            log.info("Header received: key: {}, value: {}", header.key(), new String(header.value(), StandardCharsets.UTF_8));
        }

        log.info("customer by CR is: {}", customer.toString());
        log.info("Customer json: {}", JsonFormat.printer().print(customer.toBuilder()));

        techtabu.kafka.protobuf.customer.Customer cust = new techtabu.kafka.protobuf.customer.Customer(customer);
        log.info("Customer by model CR: {}", cust);

        customerRepository.save(cust);
    }

//    @Scheduled(fixedDelay = 5000)
    public void sendMessage() {
        Customer customer = Customer.newBuilder()
                .setFirstName("Nebula")
                .setLastName("Thanos")
                .setEmail("n.thanos@universe.com")
                .build();

        log.info("sending message for customer: {}", customer);

        kafkaTemplate.send("protobuf-topic", customer.toByteArray());
    }

//    @Scheduled(fixedDelay = 5000)
    public void sendMessageViaRecord() {
        Customer customer = Customer.newBuilder()
                .setFirstName("Nebula")
//                .setLastName("Thanos")
                .setEmail("n.thanos@universe.com")
                .build();

        ProducerRecord<String, byte[]> record = new ProducerRecord<>("protobuf-topic-cr", customer.toByteArray());
        record.headers().add("message-by", "Gamoro".getBytes(StandardCharsets.UTF_8));

        log.info("sending message for customer consumer record: {}", customer);

        kafkaTemplate.send(record);
    }
}
