package techtabu.json.mapkey

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author TechTabu
 */
@RestController
@RequestMapping("/mapkey")
@Slf4j
@CrossOrigin("*")
class MapKeyController {
    @JsonSerialize(keyUsing = PersonKeyWithJSerializer::class)
    var map: Map<PersonKeyWithJ, Person>? = null

    @get:GetMapping("/")
    val allPersons: Map<PersonKey, Person>
        get() = java.util.Map.of(
            PersonKey("Joe", "West"), Person("Joe", "jwest@ccpd.com", 45000),
            PersonKey("Barry", "Allen"), Person("Barry", "ballen@starlabs.com", 88000)
        )

    @get:GetMapping("/byserializer")
    val allPersonsByPersonKeyWithJ: Map<PersonKeyWithJ, Person>?
        get() {
            map = java.util.Map.of(
                PersonKeyWithJ("Cisco", "Ramon"), Person("Cisco", "cramon@starlabs.com", 54500),
                PersonKeyWithJ("Iris", "West"), Person("Iris", "iwest@citizen.com", 78000)
            )
            return map
        }
}