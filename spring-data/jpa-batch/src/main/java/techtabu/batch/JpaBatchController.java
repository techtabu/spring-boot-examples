package techtabu.batch;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import techtabu.batch.customer.Customer;
import techtabu.batch.customer.CustomerRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author tabuthevarajan
 */

@RestController
@RequestMapping("/jpa-batch")
@Slf4j
public class JpaBatchController {

    private static List<String> firstNames = List.of("Arya", "Tabu", "Sansa", "Jon", "Bran", "Cercei", "Neel", "Kirk", "John", "Jamie",
            "Margery", "Danny", "Kenny", "Shay", "Tom", "Catelyn", "Inara", "Katy", "Mary", "Joe");

    private static List<String> lastNames = List.of("Strak", "Dev", "Lanister", "Targaryan", "Tyler", "Willioms", "Lucard", "Smith",
            "Alderson", "Dawson", "Mills", "Wade", "Green", "Theva", "Legend", "Doe", "Perry");

    private static String[] domains = {"@gmail.com", "@hotmail.com", "@yahoo.com", "@aol.com", "@google.com", "@nanthealth.com",
            "@masimo.com", "@boa.com", "@amd.com", "@me.com"};

    private final CustomerRepository customerRepository;

    public JpaBatchController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Operation(summary = "Insert number of requested customers in the DB")
    @PostMapping
    public void insertMultipleRecords(@RequestParam(required = false) Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }

        List<Customer> customers = IntStream.range(0, count)
                .mapToObj(i -> this.createCustomer())
                .collect(Collectors.toList());

        customerRepository.saveAll(customers);
    }


    private Customer createCustomer() {
        String fn = firstNames.get(new Random().nextInt(firstNames.size() - 1));
        String ln = lastNames.get(new Random().nextInt(lastNames.size() - 1));
        String domain = domains[new Random().nextInt(domains.length - 1)];

        return Customer.builder()
                .id(UUID.randomUUID().toString())
                .firstName(fn)
                .lastName(ln)
                .email(fn + "." + ln + domain)
                .customerNumber(UUID.randomUUID().toString())
                .build();
    }


}
