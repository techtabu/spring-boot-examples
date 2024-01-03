package techtabu.jpaprojections.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TechTabu
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, String>, CrudRepository<Customer, String> {

    Page<Customer> findAllByFirstName(String firstName, Pageable pageable);

    Page<CustomerDTO> findNamesOnlyByFirstName(String firstName, Pageable pageable);

    @Query("SELECT c FROM customer c where c.email LIKE %:domain")
    List<Customer> getCustomersForDomain(@Param("domain") String domain);

    @Query("SELECT c FROM customer c where c.email LIKE %:domain")
    Page<Customer> getCustomersForDomainPageable(@Param("domain") String domain, Pageable pageable);
}