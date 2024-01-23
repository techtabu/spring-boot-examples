package techtabu.postgres.jsonb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import techtabu.postgres.jsonb.user.LibraryUser;
import techtabu.postgres.jsonb.user.LibraryUserDTO;
import techtabu.postgres.jsonb.user.LibraryUserRepository;

import java.util.List;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/pg-jsonb")
@Slf4j
public class JsonBController {

    private final LibraryUserRepository libraryUserRepository;
    private final ObjectMapper objectMapper;

    public JsonBController(LibraryUserRepository libraryUserRepository, ObjectMapper objectMapper) {
        this.libraryUserRepository = libraryUserRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public LibraryUserDTO createLibraryUser(@RequestBody LibraryUserDTO libraryUserDTO) throws JsonProcessingException {
        LibraryUser libraryUser = new LibraryUser(libraryUserDTO, objectMapper);
        libraryUser =  libraryUserRepository.save(libraryUser);
        return libraryUser.toLibraryDTO(objectMapper);
    }

    @PostMapping("/save-all")
    public List<LibraryUserDTO> createMultipleUsers(@RequestBody List<LibraryUserDTO> libraryUsers) {
        return libraryUsers.stream()
                .map(lu -> {
                    try {
                        return new LibraryUser(lu, objectMapper);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(libraryUserRepository::save)
                .map(lu -> {
                    try {
                        return lu.toLibraryDTO(objectMapper);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    @GetMapping("/by-author")
    public List<LibraryUser> getUsersRentedABook(@RequestParam("author") String author) {
        return libraryUserRepository.findByLibBooksAuthor(author);
    }

    @GetMapping("selected-fields")
    public List<LibraryUserDTO> getSelectedFieldsFromCity(@RequestParam("city") String city) {
        return libraryUserRepository.getOnlySelectedFields(city).stream()
                .map(lu -> {
                    try {
                        return lu.toLibraryDTO(objectMapper);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
