package techtabu.mongo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * @author TechTabu
 */

@DataMongoTest
@Testcontainers
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class InvoiceRepositoryIT {

    public static MongoDBContainer mongoDBContainer;

    @Autowired
    InvoiceRepository invoiceRepository;

    List<Invoice> invoices = new ArrayList<>();

    static {
        mongoDBContainer = new MongoDBContainer("mongo:6.0")
                .withExposedPorts(27017);

        mongoDBContainer.start();

    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getConnectionString);
    }

    @Nested
    @Order(1)
    class LoadData {
        @Test
        public void loadDataAndVerify() {
            invoiceRepository.deleteAll();
            Address ad1 = Address.builder().street1("141 Main Road").city("Kansas City").state("KS").country("USA").zipCode(34453).build();
            Address ad2 = Address.builder().street1("147 Main Road").city("Kansas City").state("KS").country("USA").zipCode(34453).build();
            Address ad3 = Address.builder().street1("147 Main Road").city("Savanna").state("GA").country("USA").zipCode(78558).build();

            List<techtabu.mongo.Order> orders1 = List.of(
                    techtabu.mongo.Order.builder().accountNumber("account-001").orderId("order-01-1").orderStatus("SHIPPED").build(),
                    techtabu.mongo.Order.builder().accountNumber("account-001").orderId("order-01-2").orderStatus("SHIPPED").build(),
                    techtabu.mongo.Order.builder().accountNumber("account-001").orderId("order-01-3").orderStatus("SHIPPED").build()
            );
            List<techtabu.mongo.Order> orders2 = List.of(
                    techtabu.mongo.Order.builder().accountNumber("account-002").orderId("order-02-1").orderStatus("PENDING").build(),
                    techtabu.mongo.Order.builder().accountNumber("account-002").orderId("order-02-2").orderStatus("ORDERED").build(),
                    techtabu.mongo.Order.builder().accountNumber("account-002").orderId("order-02-3").orderStatus("SHIPPED").build()
            );
            List<techtabu.mongo.Order> orders3 = List.of(
                    techtabu.mongo.Order.builder().accountNumber("account-003").orderId("order-03-1").orderStatus("PENDING").build(),
                    techtabu.mongo.Order.builder().accountNumber("account-003").orderId("order-04-2").orderStatus("ORDERED").build(),
                    techtabu.mongo.Order.builder().accountNumber("account-003").orderId("order-04-3").orderStatus("ORDERED").build()
            );

            Invoice invoice1 = Invoice.builder()
                    .invoiceNumber("invoice-001")
                    .customerId("customer-001")
                    .shippingAddress(ad1)
                    .orders(orders1).build();
            invoices.add(invoice1);

            Invoice invoice2 = Invoice.builder()
                    .invoiceNumber("invoice-002")
                    .customerId("customer-002")
                    .shippingAddress(ad2)
                    .orders(orders2)
                    .build();
            invoices.add(invoice2);

            Invoice invoice3 = Invoice.builder()
                    .invoiceNumber("invoice-003")
                    .customerId("customer-001")
                    .shippingAddress(ad3)
                    .orders(orders3)
                    .build();
            invoices.add(invoice3);


            invoiceRepository.saveAll(invoices);

            await()
                    .pollInterval(Duration.ofSeconds(3))
                    .atMost(30, TimeUnit.SECONDS)
                    .untilAsserted(() -> {
                        List<Invoice> allInvoices = invoiceRepository.findAll();
                        assertThat(allInvoices).hasSize(3);
                    });
        }
    }

    @Nested
    @Order(2)
    class TestInvoiceRepository {
        @Test
        public void findByInvoiceNumber() {
            Invoice invoice = invoiceRepository.findByInvoiceNumber(invoices.get(0).getInvoiceNumber());
            assertThat(invoice.getInvoiceNumber()).isEqualTo("invoice-001");
            assertThat(invoice.getCustomerId()).isEqualTo("customer-001");
            assertThat(invoice.getOrders()).hasSize(3);
        }

        @Test
        public void findInvoiceByShippingCity() {
            List<Invoice> cityInvoices = invoiceRepository.findByShippingAddressCity("Kansas City");
            assertThat(cityInvoices).hasSize(2);
        }
    }



//
//    @Test
//    public void testGetAllInvoices() {
//        log.info("Testing InvoiceRepositoryTest::testGetAllInvoices");
//        Address ad1 = Address.builder().street1("141 Main Road").city("Kansas City").state("KS").country("USA").zipCode(34453).build();
//        List<Order> orders1 = List.of(
//                Order.builder().accountNumber("account-001").orderId("order-01-1").orderStatus("SHIPPED").build(),
//                Order.builder().accountNumber("account-001").orderId("order-01-2").orderStatus("SHIPPED").build(),
//                Order.builder().accountNumber("account-001").orderId("order-01-3").orderStatus("SHIPPED").build()
//        );
//        Invoice invoice1 = Invoice.builder()
//                .invoiceNumber("invoice-001")
//                .customerId("customer-001")
//                .shippingAddress(ad1)
//                .orders(orders1).build();
//        invoiceRepository.save(invoice1);
//
//        List<Invoice> invoices =invoiceRepository.findAll();
//        assertThat(invoices).isNotNull();
//        assertThat(invoices.size()).isEqualTo(1);
//    }
}
