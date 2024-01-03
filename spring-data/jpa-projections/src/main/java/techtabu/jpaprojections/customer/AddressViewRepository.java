package techtabu.jpaprojections.customer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TechTabu
 */

@Repository
public class AddressViewRepository {

    @PersistenceContext
    EntityManager entityManager;

    private final static String query = "SELECT a.street AS street, a.city AS city, a.state AS state, CAST(count(c.address_id) AS INTEGER) AS customer_count \n" +
            "FROM address a \n" +
            "   RIGHT JOIN customer c on c.address_id = a.id \n" +
            "GROUP BY a.street, a.city, a.state";

    public List<AddressView> getAllAddressViews() {
        Query jdbcQuery = entityManager.createNativeQuery(query, AddressView.class);
        return jdbcQuery.getResultList();
    }
}
