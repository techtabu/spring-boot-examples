package techtabu.mongo;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


/**
 * @author TechTabu
 */

@Slf4j
@Repository
public class CustomInvoiceRepository {

    final MongoTemplate mongoTemplate;
    final InvoiceRepository invoiceRepository;

    public CustomInvoiceRepository(MongoTemplate mongoTemplate,
                                   InvoiceRepository invoiceRepository) {
        this.mongoTemplate = mongoTemplate;
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice updateInvoice(String invoiceNumber, String invoiceName) {

        Query query = new Query(Criteria.where("invoiceNumber").is(invoiceNumber));
        Update update = new Update();
        update.set("invoiceName", invoiceName);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Invoice.class);
        if (result.getMatchedCount() == 0) {
            log.info("no document is updated..");
        } else {
            log.info("found: {}, but updated: {}", result.getMatchedCount(), result.getModifiedCount());
        }

        // I should be able to do this, but for keep the next line until tested.
        Invoice invoice = mongoTemplate.findOne(query, Invoice.class);

        return invoice;
//        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }
}
