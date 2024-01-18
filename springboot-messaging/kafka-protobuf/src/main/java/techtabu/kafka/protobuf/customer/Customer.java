package techtabu.kafka.protobuf.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TechTabu
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private String firstName;
    private String lastName;
    private String email;


    public Customer(CustomerMessage.Customer customer) {
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
    }
}
