package techtabu.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author TechTabu
 */

@Service
@Slf4j
public class CustomerService {

    private List<Customer> customers = new ArrayList<>(Arrays.asList(
            new Customer(1, "Tabu", "Dev", "tabu@gmail.com"),
            new Customer(2, "Shalu", "Tabu", "shalu@gmal.com")
    ));

    public List<Customer> getAll() {
        log.info("returning all customers");
        return this.customers;
    }

    public Customer getCustomer(int id) {
        return customers.stream().filter(c -> c.getId() == id).findAny().orElse(null);
    }

    public Customer addCustomer( Customer customer) {
        int id = new Random().nextInt(100000);
        customer.setId(id);
        this.customers.add(customer);
        return customer;
    }
}
