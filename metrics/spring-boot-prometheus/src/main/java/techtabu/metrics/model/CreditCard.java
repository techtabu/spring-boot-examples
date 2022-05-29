package techtabu.metrics.model;

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
public class CreditCard {

    private Long id;
    private String number;
    private String type;
    private String accountNumber;
}

