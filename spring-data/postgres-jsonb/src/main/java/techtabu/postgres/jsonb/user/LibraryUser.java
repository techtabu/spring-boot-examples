package techtabu.postgres.jsonb.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String address;

    public LibraryUser(LibraryUserDTO dto, ObjectMapper objectMapper) throws JsonProcessingException {
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.libBooks = dto.getLibBooks();
        this.address = objectMapper.writeValueAsString(dto.getAddress());
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID();
    }

    public LibraryUserDTO toLibraryDTO(ObjectMapper objectMapper) throws JsonProcessingException {
        return LibraryUserDTO.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .libBooks(new ArrayList<>(this.libBooks))
                .address(objectMapper.readValue(this.address, Address.class))
                .selectedFields(objectMapper.readValue(this.address, HashMap.class))
                .build();
    }
}
