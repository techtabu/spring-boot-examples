package techtabu.jpapageable.customer

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * @author TechTabu
 */

@Repository
interface CustomerRepository : PagingAndSortingRepository<Customer, Int> {

    fun findAllByFirstName(firstName: String, pageable: Pageable) : Page<Customer>

    @Query("SELECT c FROM customer c where c.email LIKE %:domain")
    fun getCustomersForDomain(@Param("domain") domain: String) : List<Customer>

    @Query("SELECT c FROM customer c where c.email LIKE %:domain")
    fun getCustomersForDomainPageable(@Param("domain") domain: String, pageable: Pageable) : Page<Customer>
}