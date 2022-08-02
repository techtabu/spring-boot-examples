package techtabu.json.mapkey

import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author TechTabu
 */
class PersonKey(firstName: String, lastName: String) {
    private val firstName: String? = firstName
    private val lastName: String? = lastName
    @JsonValue
    override fun toString(): String {
        return "{\"firstName\": \"$firstName\", \"lastName\": \"$lastName\"}"
    } //    @JsonValue
    //    public String convertToKey() {
    //        return "firstName: " + firstName + ", lastName: " + lastName;
    //    }
}