package techtabu.jpapageable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import techtabu.jpapageable.customer.Customer;
import techtabu.jpapageable.customer.CustomerRepository;
import techtabu.jpapageable.utils.CustomPageImpl;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author TechTabu
 */

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CustomerControllerTest {

    @Autowired CustomerRepository customerRepository;
    @Autowired MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    static List<String> firstNames = List.of("Arya", "Tabu", "Sansa", "Jon", "Bran", "Cercei", "Neel", "Kirk", "John", "Jamie",
            "Margery", "Danny", "Kenny", "Shay", "Tom", "Catelyn", "Inara", "Katy", "Mary", "Joe");

    static List<String> lastNames = List.of("Strak", "Dev", "Lanister", "Targaryan", "Tyler", "Willioms", "Lucard", "Smith",
            "Alderson", "Dawson", "Mills", "Wade", "Green", "Theva", "Legend", "Doe", "Perry");

    static String[] domains = {"@gmail.com", "@hotmail.com", "@yahoo.com", "@aol.com", "@google.com", "@nanthealth.com",
            "@masimo.com", "@boa.com", "@amd.com", "@me.com"};

    @Test
    public void testGetAll() throws Exception {
        log.info("testing testGetAll");

        IntStream.range(0, 50)
                .forEach(i -> {
                    Customer customer = createCustomer();
                    log.trace("Inserting: {}", customer.toString());
                    customerRepository.save(customer);
                });

        var response = mockMvc.perform(get("/pageable")
                        .param("page", "0")
                        .param("size", "10"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info("customers: {}", response);
        CustomPageImpl<Customer> page = objectMapper.readValue(response, new TypeReference<>() {});
        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getTotalPages()).isEqualTo(5);
        assertThat(page.getContent().size()).isEqualTo(10);

        response = mockMvc.perform(get("/pageable")
                        .param("page", "1")
                        .param("size", "15"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        page = objectMapper.readValue(response, new TypeReference<>() {});
        log.info("response for page = 1 & pagesize = 15 is :: totalElements: {}, total pages: {}, content size: {}",
                page.getTotalElements(), page.getTotalPages(), page.getContent().size());
        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.getContent().size()).isEqualTo(15);
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
