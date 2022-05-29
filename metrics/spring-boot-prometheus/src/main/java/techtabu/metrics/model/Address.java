package techtabu.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author TechTabu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    private Long id;

    private String street1;
    private String street2;
    private String city;
    private String state;
    private String country;
    private Integer zipCode;
    private String addressType;
    private String accountNumber;
}
