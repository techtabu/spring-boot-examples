package techtabu.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TechTabu
 */

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {

    Invoice findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByOrdersOrderStatus(String orderStatus);
    List<Invoice> findByShippingAddressCity(String city);

    @Query(value = "{invoiceNumber:'?0'}", fields = "{'customerId' : 1, 'shippingAddress': 1}")
    Invoice findSpecificFields(String invoiceNumber);
}
