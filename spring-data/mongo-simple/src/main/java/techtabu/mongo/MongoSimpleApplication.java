package techtabu.mongo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MongoSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoSimpleApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(value = "data.initialize", havingValue = "true")
    CommandLineRunner commandLineRunner(DataInitializer dataInitializer) {
        return args -> {
            dataInitializer.populateData();
        };
    }

}
