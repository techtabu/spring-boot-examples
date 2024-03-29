package techtabu.reactive;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/reactive-mongo-with-web")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "create one customer from @RequestBody")
    @PostMapping
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @Operation(summary = "Create and save random customer")
    @PostMapping("/random-one")
    public Mono<Customer> saveRandomCustomer() {
        return customerService.saveCustomer(createRandomCustomer());
    }

    @Operation(summary = "Create number of random customers from @RequestParam")
    @PostMapping("/create-numbers")
    public void createNumberOfCustomers(@RequestParam Integer count) {
        IntStream.range(0, count)
                .forEach(i -> customerService.saveCustomer(createRandomCustomer()).subscribe());
    }

    @Operation(summary = "return all customers from DB, with delay from @RequestParam")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> findAll(@RequestParam("delay") Long delay) {
        return customerService.getAllCustomers(delay);
    }

    @Operation(summary = "return # of customers from @size and page number  with @delay")
    @GetMapping(value = "/paginated", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> findAllPaginated(@RequestParam("delay") Long delay,
                                           @RequestParam int page,
                                           @RequestParam int size) {
        return customerService.getAllCustomersPaginated(delay, page, size);
    }

    private Customer createRandomCustomer() {
        return Customer.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .age(new Random().nextInt(100))
                .income(new Random().nextDouble(100000.0))
                .build();
    }

}
