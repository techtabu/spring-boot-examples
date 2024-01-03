package techtabu.jpaprojections;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "JPA Pageable",
                version = "0.1.0-SNAPSHOT"
        )
)
public class JpaProjectionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaProjectionsApplication.class, args);
    }


    @Bean
    @ConditionalOnProperty(name = "data.initialize", havingValue = "true")
    CommandLineRunner commandLineRunner(DataInitializer dataInitializer) {
        return args -> {
            dataInitializer.populate();
        };
    }

}
