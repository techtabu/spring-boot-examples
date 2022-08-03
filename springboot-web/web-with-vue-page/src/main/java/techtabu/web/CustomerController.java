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

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable int id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        customer = customerService.addCustomer(customer);
        return customer;
    }
}
