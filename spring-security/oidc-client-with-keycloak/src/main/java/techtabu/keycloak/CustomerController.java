package techtabu.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
@Slf4j
public class CustomerController {

    private List<Customer> customers = new ArrayList<>(Arrays.asList(
            new Customer(1, "Tabu", "Dev", "tabu@gmail.com", ""),
            new Customer(2, "Shalu", "Tabu", "shalu@gmal.com", "")
    ));

    @GetMapping
    public List<Customer> getAllCustomers(OAuth2AuthenticationToken auth) {
        log.info("Auth: {}", auth);
        String name = auth.getPrincipal().getAttribute("name");
        customers.forEach(c -> c.setUser(name));
        return this.customers;
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        customer.setId(new Random().nextInt(100000));
        this.customers.add(customer);
        log.info("{} is added", customer);
        return customer;
    }
}
