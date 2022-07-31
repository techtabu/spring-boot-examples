package techtabu.json.mapkey;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author TechTabu
 */

@Data
@AllArgsConstructor
public class PersonKeyWithJ {

    private String firstName;
    private String lastName;

    @JsonValue
    public String convertToKey() {
        return "{firstName: " + firstName + ", lastName: " + lastName + "}";
    }
}
