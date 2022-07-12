package techtabu.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author TechTabu
 */

@Component
@Slf4j
public class DataInitializer {

    private final InvoiceRepository invoiceRepository;

    public DataInitializer(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void populateData() {
        log.info("initializing data in db..");
        invoiceRepository.deleteAll();
        Address ad1 = Address.builder().street1("141 Main Road").city("Kansas City").state("KS").country("USA").zipCode(34453).build();
        Address ad2 = Address.builder().street1("147 Main Road").city("Kansas City").state("KS").country("USA").zipCode(34453).build();
        Address ad3 = Address.builder().street1("147 Main Road").city("Savanna").state("GA").country("USA").zipCode(78558).build();

        Invoice invoice1 = Invoice.builder()
                .invoiceNumber("invoice-001")
                .customerId("customer-001")
                .shippingAddress(ad1)
                .orders(List.of(
                        Order.builder().accountNumber("account-001").orderId("order-01-1").orderStatus("SHIPPED").build(),
                        Order.builder().accountNumber("account-001").orderId("order-01-2").orderStatus("SHIPPED").build(),
                        Order.builder().accountNumber("account-001").orderId("order-01-3").orderStatus("SHIPPED").build()
                )).build();
        invoiceRepository.save(invoice1);
        log.info("Initialized invoice 1: {}", invoice1);

        Invoice invoice2 = Invoice.builder()
                .invoiceNumber("invoice-002")
                .customerId("customer-002")
                .shippingAddress(ad2)
                .orders(List.of(
                        Order.builder().accountNumber("account-002").orderId("order-02-1").orderStatus("PENDING").build(),
                        Order.builder().accountNumber("account-002").orderId("order-02-2").orderStatus("ORDERED").build(),
                        Order.builder().accountNumber("account-002").orderId("order-02-3").orderStatus("SHIPPED").build()
                )).build();
        invoiceRepository.save(invoice2);
        log.info("Initialized invoice 2: {}", invoice2);

        Invoice invoice3 = Invoice.builder()
                .invoiceNumber("invoice-003")
                .customerId("customer-001")
                .shippingAddress(ad3)
                .orders(List.of(
                        Order.builder().accountNumber("account-003").orderId("order-03-1").orderStatus("PENDING").build(),
                        Order.builder().accountNumber("account-003").orderId("order-04-2").orderStatus("ORDERED").build(),
                        Order.builder().accountNumber("account-003").orderId("order-04-3").orderStatus("ORDERED").build()
                )).build();
        invoiceRepository.save(invoice3);
        log.info("Initialized invoice 3: {}", invoice3);

    }
}
