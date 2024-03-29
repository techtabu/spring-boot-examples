package techtabu.jpaprojections.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * @author TechTabu
 */

@Entity(name = "customer")
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Customer {

    @Id
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String customerNumber;
    private String email;

    private String addressId;

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString();
    }
}
