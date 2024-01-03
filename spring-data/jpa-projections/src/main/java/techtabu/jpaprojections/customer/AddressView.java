package techtabu.jpaprojections.customer;

import jakarta.persistence.Column;
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
public class AddressView {

    private String street;
    private String city;
    private String state;
    @Column(name = "customer_count")
    private Integer customerCount;
}
