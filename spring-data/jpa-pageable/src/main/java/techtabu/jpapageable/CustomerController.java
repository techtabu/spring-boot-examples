package techtabu.jpapageable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import techtabu.jpapageable.customer.Customer;
import techtabu.jpapageable.customer.CustomerRepository;

import java.util.List;

/**
 * @author TechTabu
 */
@RestController
@RequestMapping("/pageable")
@Slf4j
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public Page<Customer> getAllCustomers(@RequestParam int page,
                                          @RequestParam int size) {

        Sort sort = Sort.by("firstName").ascending().and(Sort.by("lastName"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customers = customerRepository.findAll(pageable);
        log.info("Returning {} customers out of {}", customers.getContent().size(), customers.getTotalElements());
        return customers;
    }

    @GetMapping("first")
    public Page<Customer> getCustomersByFirstName(@RequestParam int page,
                                                  @RequestParam int size,
                                                  @RequestParam String firstName) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findAllByFirstName(firstName, pageable);
        log.info("Returning {} customers out of {}", customers.getContent().size(), customers.getTotalElements());

        return customers;
    }

    @GetMapping("/domain")
    public List<Customer> getCustomersForDomain(@RequestParam String domain) {
        List<Customer> customers = customerRepository.getCustomersForDomain(domain);
        log.info("Number of customers: {}", customers.size());

        return customers;
    }

    @GetMapping("/pageable/domain")
    public Page<Customer> getCustomersForDomainWithPageable(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam String domain) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers =  customerRepository.getCustomersForDomainPageable(domain, pageable);
        log.info("Returning {} customers out of {}", customers.getContent().size(), customers.getTotalElements());

        return customers;
    }
}
