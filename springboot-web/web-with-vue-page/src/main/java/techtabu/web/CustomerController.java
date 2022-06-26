package techtabu.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
@Slf4j
public class CustomerController {

    private List<Customer> customers = new ArrayList<>(Arrays.asList(
            new Customer(1, "Tabu", "Dev", "tabu@gmail.com"),
            new Customer(2, "Shalu", "Tabu", "shalu@gmal.com")
    ));

    @GetMapping
    public List<Customer> getAll() {
        return this.customers;
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable int id) {
        return customers.stream().filter(c -> c.getId() == id).findAny().orElse(null);
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        int id = new Random().nextInt(100000);
        customer.setId(id);
        this.customers.add(customer);
        return customer;
    }
}
