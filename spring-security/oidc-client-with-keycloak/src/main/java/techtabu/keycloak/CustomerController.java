package techtabu.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    private List<Customer> customers = new ArrayList<>(Arrays.asList(
            new Customer("Tabu", "New York", ""),
            new Customer("Shalu", "Miami", "")
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
        this.customers.add(customer);
        log.info("{} is added", customer);
        return customer;
    }
}
