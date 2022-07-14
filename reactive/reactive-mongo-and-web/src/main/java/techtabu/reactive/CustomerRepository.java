package techtabu.reactive;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


/**
 * @author TechTabu
 *
 * ReatuveSortingRepository adds sorting capability to the ReactiveRepository.
 * simple as that.
 */

@Repository
public interface CustomerRepository extends ReactiveSortingRepository<Customer, String> {
//public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Flux<Customer> findAllBy(Pageable pageable);
}
