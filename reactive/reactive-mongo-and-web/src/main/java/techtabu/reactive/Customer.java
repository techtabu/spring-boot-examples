package techtabu.reactive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author TechTabu
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("customers")
public class Customer {

    @Id
    private String id;

    private String firstName, lastName, email;
    private int age;
    private double income;
}
