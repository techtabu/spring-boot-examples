package techtabu.jpapageable

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import techtabu.jpapageable.customer.Customer
import techtabu.jpapageable.customer.CustomerRepository
import java.util.UUID
import kotlin.random.Random


/**
 * @author TechTabu
 */

@Component
@ConditionalOnProperty(value = ["data.initialize"], havingValue = "true")
class DataInitializer @Autowired constructor(val customerRepository: CustomerRepository) : CommandLineRunner {

    private val log = KotlinLogging.logger{}

    val firstNames = listOf("Arya", "Tabu", "Sansa", "Jon", "Bran", "Cercei", "Neel", "Kirk", "John", "Jamie",
        "Margery", "Danny", "Kenny", "Shay", "Tom", "Catelyn", "Inara", "Katy", "Mary", "Joe")

    val lastNames = listOf("Strak", "Dev", "Lanister", "Targaryan", "Tyler", "Willioms", "Lucard", "Smith",
        "Alderson", "Dawson", "Mills", "Wade", "Green", "Theva", "Legend", "Doe", "Perry")

    val domains = listOf("@gmail.com", "@hotmail.com", "@yahoo.com", "@aol.com", "@google.com", "@nanthealth.com",
        "@masimo.com", "@boa.com", "@amd.com", "@me.com")

    fun loadData() {
        log.info("loading initial data... ")
        var customers : MutableList<Customer> = mutableListOf()
        (1..1000).forEach { _ ->
            val customer = createCustomer()
            customerRepository.save(customer)
            customers.add(customer)
        }

        log.info("Saved ${customers.size}s of customers")
    }

    fun createCustomer() : Customer {
        val fn = firstNames[Random.nextInt(0, firstNames.size - 1)]
        val ln = lastNames[Random.nextInt(0, lastNames.size - 1)]
        val dom = domains[Random.nextInt(0, domains.size - 1)]

        return Customer(id = 0,
            firstName = fn,
            lastName = ln,
            customerNumber = UUID.randomUUID().toString(),
            email = "$fn.$ln@$dom"
        )
    }

    override fun run(vararg args: String?) {
        loadData()
    }
}