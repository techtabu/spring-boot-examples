package techtabu.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TechTabu
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private String street1, street2, state, city, country;
    private Integer zipCode;
}
