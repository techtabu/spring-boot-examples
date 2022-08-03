package techtabu.jpapageable

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import techtabu.jpapageable.customer.Customer
import techtabu.jpapageable.customer.CustomerRepository


/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/pageable")
class CustomerController @Autowired constructor(
        val customerRepository: CustomerRepository
){
    private val log = KotlinLogging.logger{}

    @GetMapping
    fun getAllCustomers(@RequestParam page : Int,
                        @RequestParam size : Int) : Page<Customer> {
        val sort = Sort.by("firstName").ascending().and(Sort.by("lastName"))
        val pageable = PageRequest.of(page, size, sort)
        val customers = customerRepository.findAll(pageable)
        log.info ("Returning ${customers.content.size} customers out of ${customers.totalElements}")

        return customers;
    }

    @GetMapping("/first")
    fun getCustomersByFirstName(@RequestParam page : Int,
                                @RequestParam size : Int,
                                @RequestParam firstName : String) : Page<Customer> {
        val pageable = PageRequest.of(page, size)
        val customers = customerRepository.findAllByFirstName(firstName, pageable)

        log.info("Returning ${customers.content.size} customers out of ${customers.totalElements} for first name $firstName")

        return customers;
    }

    @GetMapping("/domain")
    fun getCustomersForDomain(@RequestParam domain: String) : List<Customer> {
        val customers = customerRepository.getCustomersForDomain(domain)
        log.info("Returning ${customers.size} for domain: $domain")

        return customers
    }

    @GetMapping("/page-domain")
    fun getPagedCustomersForDomain(@RequestParam page : Int,
                                    @RequestParam size : Int,
                                    @RequestParam domain: String) {
        val pageable = PageRequest.of(page, size)
        val customers = customerRepository.getCustomersForDomainPageable(domain, pageable)

        log.info("Returning ${customers.content.size} customers out of ${customers.totalElements} for domain $domain")

    }
}