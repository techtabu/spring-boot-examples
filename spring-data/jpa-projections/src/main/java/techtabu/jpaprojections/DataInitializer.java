package techtabu.jpaprojections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import techtabu.jpaprojections.customer.Address;
import techtabu.jpaprojections.customer.AddressRepository;
import techtabu.jpaprojections.customer.Customer;
import techtabu.jpaprojections.customer.CustomerRepository;

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

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(AddressRepository addressRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    private static List<String> firstNames = List.of("Arya", "Tabu", "Sansa", "Jon", "Bran", "Cercei", "Neel", "Kirk", "John", "Jamie",
            "Margery", "Danny", "Kenny", "Shay", "Tom", "Catelyn", "Inara", "Katy", "Mary", "Joe");

    private static List<String> lastNames = List.of("Stark", "Dev", "Lanister", "Targaryan", "Tyler", "Willioms", "Lucard", "Smith",
            "Alderson", "Dawson", "Mills", "Wade", "Green", "Theva", "Legend", "Doe", "Perry");

    private static String[] domains = {"@gmail.com", "@hotmail.com", "@yahoo.com", "@aol.com", "@google.com", "@nanthealth.com",
            "@masimo.com", "@boa.com", "@amd.com", "@me.com"};

    private static String[] streetAddresses = {"1 Main St", "2 Side Street", "3 ML King Str", "4 NY Ave",
            "5 Colorado Rd", "6 Maine Ave", "7 Florida Circle", "8 Banyan Rd", "9 Georgia Rd", "10 Nevada Ave",
            "11 Texas Rd", "12 5th Ave", "13 Maria Rd"};

    private static String[] cities = {"Callaway", "Tallhassee", "Jacksonville", "Atlanta", "Orlando", "Irvine",
            "San Fansisco", "New york", "Dallas", "Denver", "Crystall River", "Miami", "Tampa"};

    private static String[] states = {"Texas", "Florida", "New york", "California", "Georgia", "North Carolina", "Maine",
            "Nevada", "Rhode Island"};


    public void populate() {
        IntStream.range(0, 100)
                .mapToObj(i -> createAndSaveAddress())
                .forEach(a -> {
                    int num = new Random().nextInt(5);
                    createCustomers(num, a.getId());
                });
        log.info("populated all records in db");
    }


    public void createCustomers(int num, String addressId) {
        IntStream.range(0, num)
                .forEach(i -> {
                    createCustomer(addressId);
                });
    }

    public Customer createCustomer(String addressId) {
        String fn = firstNames.get(new Random().nextInt(firstNames.size() - 1));
        String ln = lastNames.get(new Random().nextInt(lastNames.size() - 1));
        String domain = domains[new Random().nextInt(domains.length - 1)];

        Customer customer = Customer.builder()
                .firstName(fn)
                .lastName(ln)
                .email(fn + "." + ln + domain)
                .customerNumber(UUID.randomUUID().toString())
                .addressId(addressId)
                .build();
        return customerRepository.save(customer);
    }

    public Address createAndSaveAddress() {
        String street = streetAddresses[new Random().nextInt(streetAddresses.length - 1)];
        String city = cities[new Random().nextInt(cities.length - 1)];
        String state = states[new Random().nextInt(states.length - 1)];

        Address address = Address.builder()
                .street(street)
                .state(state)
                .city(city)
                .build();

        return addressRepository.save(address);
    }


}
