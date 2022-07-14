package techtabu.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;

/**
 * @author TechTabu
 */

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CustomerService(CustomerRepository customerRepository,
                           ReactiveMongoTemplate reactiveMongoTemplate) {
        this.customerRepository = customerRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Mono<Customer> saveCustomer(Customer customer) {
        log.info("saving customer: {}", customer);
        return customerRepository.save(customer);
    }

    public Flux<Customer> getAllCustomers(long delay) {
        Flux<Customer> customers = customerRepository
                .findAll()
                .delayElements(Duration.ofMillis(delay));
        return customers;
    }

    public Flux<Customer> getAllCustomersPaginated(long delay, int page, int size) {
        Flux<Customer> customers = customerRepository
                .findAllBy(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "age")))
                .delayElements(Duration.ofMillis(delay));
        return customers;
    }

    public Mono<Customer> updateCustomer(String customerId, Customer customer) {
        return customerRepository.findById(customerId)
                .flatMap(cust -> {
                    cust.setFirstName(customer.getFirstName());
                    cust.setLastName(customer.getLastName());
                    return customerRepository.save(cust);
                });
    }

    public Mono<Customer> deleteUser(String userId){
        return customerRepository.findById(userId)
                .flatMap(existingUser -> customerRepository.delete(existingUser)
                        .then(Mono.just(existingUser)));
    }

    public Flux<Customer> sortCustomers(String sortField) {
        Query query = new Query()
                .with(Sort
                        .by(Collections.singletonList(Sort.Order.asc(sortField)))
                );
//        query.addCriteria(Criteria
//                .where("name")
//                .regex(name)
//        );

        return reactiveMongoTemplate
                .find(query, Customer.class);
    }
}
