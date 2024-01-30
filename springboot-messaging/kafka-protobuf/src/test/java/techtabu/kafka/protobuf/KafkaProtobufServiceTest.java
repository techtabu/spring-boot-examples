package techtabu.kafka.protobuf;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import techtabu.kafka.protobuf.customer.Customer;
import techtabu.kafka.protobuf.customer.CustomerMessage;
import techtabu.kafka.protobuf.customer.CustomerRepository;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

/**
 * @author TechTabu
 */

@SpringBootTest
@TestPropertySource(
    properties = {
        "spring.kafka.consumer.auto-offset-reset=earliest"
    }
)
@Testcontainers
@Slf4j
public class KafkaProtobufServiceTest {

    @Autowired KafkaTemplate<String, byte[]> kafkaTemplate;
    @Autowired CustomerRepository customerRepository;

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.3.0")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    public void testPublishMessage() {

        CustomerMessage.Customer customer = CustomerMessage.Customer.newBuilder()
                .setFirstName("Nebula")
                .setLastName("Thanos")
                .setEmail("n.thanos@universe.com")
                .build();

        ProducerRecord<String, byte[]> record = new ProducerRecord<>("protobuf-topic-cr", customer.toByteArray());
        record.headers().add("message-by", "Gamoro".getBytes(StandardCharsets.UTF_8));

        log.info("sending message for customer consumer record: {}", customer);

        kafkaTemplate.send(record);

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(10, SECONDS)
            .untilAsserted(() -> {
                Optional<Customer> optionalCustomer = customerRepository.findByEmail(customer.getEmail());
                assertThat(optionalCustomer).isPresent();
                assertThat(optionalCustomer.get().getFirstName()).isEqualTo("Nebula");
                assertThat(optionalCustomer.get().getLastName()).isEqualTo("Thanos");
            });


    }
}
