package techtabu.hazelcast;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author TechTabu
 */

@Service
@Slf4j
public class HazelcastService {

    private final BookRepository bookRepository;

    public HazelcastService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Cacheable(cacheNames = "bookByName", key = "#name", unless = "#result == null")
    public Book getByName(String name) {
        return bookRepository.findByName(name);
    }

    @Cacheable("author")
    public String getAuthor(String author) {
        return getAuthorSlow(author);
    }

    private String getAuthorSlow(String author) {
        log.info("accessed slow return for {}", author);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return author;
    }
}
