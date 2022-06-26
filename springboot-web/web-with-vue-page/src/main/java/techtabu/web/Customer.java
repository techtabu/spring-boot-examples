package techtabu.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TechTabu
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
}
