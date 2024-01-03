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

@Entity(name = "address")
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Address {

    @Id
    private String id;

    private String street;
    private String city;
    private String state;

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString();
    }
}
