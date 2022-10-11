package techtabu.jpapageable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import techtabu.jpapageable.customer.Customer;
import techtabu.jpapageable.customer.CustomerRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author TechTabu
 */
@Component
@Slf4j
public class DataInitializer {

    private final CustomerRepository customerRepository;

    public DataInitializer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private static List<String> firstNames = List.of("Arya", "Tabu", "Sansa", "Jon", "Bran", "Cercei", "Neel", "Kirk", "John", "Jamie",
            "Margery", "Danny", "Kenny", "Shay", "Tom", "Catelyn", "Inara", "Katy", "Mary", "Joe");

    private static List<String> lastNames = List.of("Strak", "Dev", "Lanister", "Targaryan", "Tyler", "Willioms", "Lucard", "Smith",
            "Alderson", "Dawson", "Mills", "Wade", "Green", "Theva", "Legend", "Doe", "Perry");

    private static String[] domains = {"@gmail.com", "@hotmail.com", "@yahoo.com", "@aol.com", "@google.com", "@nanthealth.com",
            "@masimo.com", "@boa.com", "@amd.com", "@me.com"};


    public void populate() {
        IntStream.range(0, 1000)
                .forEach(i -> {
                    Customer customer = createCustomer();
                    log.trace("Inserting: {}", customer.toString());
                    customerRepository.save(customer);
                });
        log.info("populated 1000 records of customers in db");
    }

    public static Customer createCustomer() {
        String fn = firstNames.get(new Random().nextInt(firstNames.size() - 1));
        String ln = lastNames.get(new Random().nextInt(lastNames.size() - 1));
        String domain = domains[new Random().nextInt(domains.length - 1)];

        return Customer.builder()
                .firstName(fn)
                .lastName(ln)
                .email(fn + "." + ln + domain)
                .customerNumber(UUID.randomUUID().toString())
                .build();
    }
}
