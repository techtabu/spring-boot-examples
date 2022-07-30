package techtabu.json.mapkey;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author TechTabu
 */

@Data
@AllArgsConstructor
public class PersonKey {

    private String firstName;
    private String lastName;

    @JsonValue
    @Override
    public String toString() {
        return "{firstName: " + firstName + ", lastName: " + lastName + "}";
    }

//    @JsonValue
//    public String convertToKey() {
//        return "firstName: " + firstName + ", lastName: " + lastName;
//    }
}
