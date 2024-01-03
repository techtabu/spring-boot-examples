package techtabu.jpaprojections.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TechTabu
 */

@Repository
public interface AddressRepository extends PagingAndSortingRepository<Address, String>, CrudRepository<Address, String> {

    List<AddressDTO> findDistinctByState(String state);
}
