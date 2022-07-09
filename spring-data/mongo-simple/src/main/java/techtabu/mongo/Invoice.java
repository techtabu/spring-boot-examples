package techtabu.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TechTabu
 */

@Document("store_invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    private String id;

    private String invoiceNumber;
    private String customerId;
    private String invoiceName;

    private List<Order> orders = new ArrayList<>();
    private Address shippingAddress;
}
