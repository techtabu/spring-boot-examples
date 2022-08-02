package techtabu.json.mapkey

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author TechTabu
 */
@RestController
@RequestMapping("/mapkey")
@CrossOrigin("*")
class MapKeyController {
    @JsonSerialize(keyUsing = PersonKeyWithJSerializer::class)
    var map: Map<PersonKeyWithJ, Person>? = null

    @get:GetMapping("/")
    val allPersons: Map<PersonKey, Person>
        get() = mapOf(
            PersonKey("Joe", "West") to Person("Joe", "jwest@ccpd.com", 45000),
            PersonKey("Barry", "Allen") to Person("Barry", "ballen@starlabs.com", 88000)
        )

    @get:GetMapping("/byserializer")
    val allPersonsByPersonKeyWithJ: Map<PersonKeyWithJ, Person>?
        get() {
            map = mapOf(
                PersonKeyWithJ("Cisco", "Ramon") to Person("Cisco", "cramon@starlabs.com", 54500),
                PersonKeyWithJ("Iris", "West") to Person("Iris", "iwest@citizen.com", 78000)
            )
            return map
        }
}