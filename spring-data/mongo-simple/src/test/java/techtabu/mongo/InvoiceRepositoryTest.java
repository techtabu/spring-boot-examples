package techtabu.mongo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author TechTabu
 */

@DataMongoTest
@Slf4j
public class InvoiceRepositoryTest {

    @Autowired InvoiceRepository invoiceRepository;

    @Test
    public void testGetAllInvoices() {
        log.info("Testing InvoiceRepositoryTest::testGetAllInvoices");
        Address ad1 = Address.builder().street1("141 Main Road").city("Kansas City").state("KS").country("USA").zipCode(34453).build();
        List<Order> orders1 = List.of(
                Order.builder().accountNumber("account-001").orderId("order-01-1").orderStatus("SHIPPED").build(),
                Order.builder().accountNumber("account-001").orderId("order-01-2").orderStatus("SHIPPED").build(),
                Order.builder().accountNumber("account-001").orderId("order-01-3").orderStatus("SHIPPED").build()
        );
        Invoice invoice1 = Invoice.builder()
                .invoiceNumber("invoice-001")
                .customerId("customer-001")
                .shippingAddress(ad1)
                .orders(orders1).build();
        invoiceRepository.save(invoice1);

        List<Invoice> invoices =invoiceRepository.findAll();
        assertThat(invoices).isNotNull();
        assertThat(invoices.size()).isEqualTo(1);
    }
}
