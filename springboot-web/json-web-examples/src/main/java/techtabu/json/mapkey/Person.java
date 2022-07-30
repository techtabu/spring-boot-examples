package techtabu.json.mapkey;

import lombok.Builder;
import lombok.Data;

/**
 * @author TechTabu
 */

@Data
@Builder
public class Person {

    String firstName;
    String email;
    int salary;
}
