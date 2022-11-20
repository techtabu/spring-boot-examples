package techtabu.messaging;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}
)
@Slf4j
class KafkaApplicationTests {

    @SpyBean KafkaService kafkaService;
    @Captor
    ArgumentCaptor<String> messageCaptor;

    @Test
    void testKafka() {
        log.info("Testing testKafka");
        kafkaService.send("sample test message");
    }

}
