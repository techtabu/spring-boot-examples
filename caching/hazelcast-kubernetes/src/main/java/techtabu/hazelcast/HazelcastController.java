package techtabu.hazelcast;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/hazelcast")
@Slf4j
public class HazelcastController {

    private final HazelcastService service;

    public HazelcastController(HazelcastService service) {
        this.service = service;
    }

    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return service.saveBook(book);
    }

    @GetMapping("/name/{name}")
    public Book getByName(@PathVariable String name) {
        return service.getByName(name);
    }

    @GetMapping("/author/{author}")
    public String getAuthor(@PathVariable String author) {
        return service.getAuthor(author);
    }
}
