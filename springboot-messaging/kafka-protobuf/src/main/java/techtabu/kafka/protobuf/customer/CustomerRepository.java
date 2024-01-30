package techtabu.kafka.protobuf.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author TechTabu
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer>, CrudRepository<Customer, Integer> {

    Page<Customer> findAllByFirstName(String firstName, Pageable pageable);

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM customer c where c.email LIKE %:domain")
    List<Customer> getCustomersForDomain(@Param("domain") String domain);

    @Query("SELECT c FROM customer c where c.email LIKE %:domain")
    Page<Customer> getCustomersForDomainPageable(@Param("domain") String domain, Pageable pageable);
}
