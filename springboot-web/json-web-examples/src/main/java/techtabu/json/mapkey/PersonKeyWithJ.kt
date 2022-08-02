package techtabu.json.mapkey

import com.fasterxml.jackson.annotation.JsonValue
import lombok.AllArgsConstructor
import lombok.Data

/**
 * @author TechTabu
 */
@Data
@AllArgsConstructor
class PersonKeyWithJ(firstName: String, lastName: String) {
    private val firstName: String? = firstName
    private val lastName: String? = lastName
    @JsonValue
    fun convertToKey(): String {
        return "{firstName: $firstName, lastName: $lastName}"
    }
}