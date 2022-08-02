package techtabu.json.mapkey

import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author TechTabu
 */
class PersonKeyWithJ(firstName: String, lastName: String) {
    private val firstName: String? = firstName
    private val lastName: String? = lastName
    @JsonValue
    fun convertToKey(): String {
        return "{firstName: $firstName, lastName: $lastName}"
    }
}