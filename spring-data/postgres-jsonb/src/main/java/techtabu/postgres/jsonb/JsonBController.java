package techtabu.postgres.jsonb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import techtabu.postgres.jsonb.user.LibraryUser;
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

    public JsonBController(LibraryUserRepository libraryUserRepository) {
        this.libraryUserRepository = libraryUserRepository;
    }

    @PostMapping
    public LibraryUser createLibraryUser(@RequestBody LibraryUser libraryUser) {
        return libraryUserRepository.save(libraryUser);
    }

    @PostMapping("/save-all")
    public List<LibraryUser> createMultipleUsers(@RequestBody List<LibraryUser> libraryUsers) {
        return libraryUsers.stream()
                .map(libraryUserRepository::save)
                .toList();
    }

    @GetMapping("/by-author")
    public List<LibraryUser> getUsersRentedABook(@RequestParam("author") String author) {
        return libraryUserRepository.findByLibBooksAuthor(author);
    }
}
