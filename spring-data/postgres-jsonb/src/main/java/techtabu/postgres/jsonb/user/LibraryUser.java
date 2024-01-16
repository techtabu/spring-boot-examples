package techtabu.postgres.jsonb.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

/**
 * @author TechTabu
 */

@Entity(name = "library_user")
@Table(name = "library_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LibraryUser {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<Book> libBooks;

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID();
    }
}
