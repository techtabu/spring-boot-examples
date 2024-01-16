package techtabu.postgres.jsonb;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Postgres JSONB",
                version = "0.1.0-SNAPSHOT"
        )
)
public class PostgresJsonbApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostgresJsonbApplication.class, args);
    }

}
