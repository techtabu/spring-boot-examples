package techtabu.postgres.jsonb.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author TechTabu
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LibraryUserDTO {

    private UUID id;

    private String firstName;
    private String lastName;
    private List<Book> libBooks;
    private Address address;
}
