package techtabu.jpaprojections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import techtabu.jpaprojections.customer.*;

import java.util.List;

/**
 * @author TechTabu
 */
@RestController
@RequestMapping("/projections")
@Slf4j
public class CustomerController {

    private final AddressRepository addressRepository;
    private final AddressViewRepository addressViewRepository;
    private final CustomerRepository customerRepository;

    public CustomerController(AddressRepository addressRepository,
                              AddressViewRepository addressViewRepository,
                              CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.addressViewRepository = addressViewRepository;
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

    @GetMapping("nameonly")
    public Page<CustomerDTO> getCustomerNamesOnlyByFirstName(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam String firstName) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerDTO> customers = customerRepository.findNamesOnlyByFirstName(firstName, pageable);
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

    @GetMapping("/addresses")
    public Page<Address> getAllAddresses(@RequestParam int page,
                                          @RequestParam int size) {

        Sort sort = Sort.by("city").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Address> addresses = addressRepository.findAll(pageable);
        log.info("Returning {} customers out of {}", addresses.getContent().size(), addresses.getTotalElements());
        return addresses;
    }

    @GetMapping("/address/{state}")
    public List<AddressDTO> getCitiesForState(@PathVariable("state") String state) {
        return addressRepository.findDistinctByState(state);
    }

    @GetMapping("/address-view")
    public List<AddressView> getAllAddressView() {
        return addressViewRepository.getAllAddressViews();
    }
}
