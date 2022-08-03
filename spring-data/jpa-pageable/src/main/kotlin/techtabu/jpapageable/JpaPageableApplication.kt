package techtabu.jpapageable

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "JPA Pageable",
        version = "0.1.0-SNAPSHOT"
    )
)
class JpaPageableApplication

fun main(args: Array<String>) {
    runApplication<JpaPageableApplication>(*args)
}
