package techtabu.hazelcast;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author TechTabu
 */

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Book findByName(String name);
}
