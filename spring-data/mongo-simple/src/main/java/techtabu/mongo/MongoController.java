package techtabu.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/mongo-simple")
@Slf4j
public class MongoController {

    private final InvoiceRepository invoiceRepository;
    private final CustomInvoiceRepository customInvoiceRepository;

    public MongoController(InvoiceRepository invoiceRepository,
                           CustomInvoiceRepository customInvoiceRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customInvoiceRepository = customInvoiceRepository;
    }

    @GetMapping
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{invoiceNumber}")
    public Invoice findByInvoiceNumber(@PathVariable String invoiceNumber) {
        log.info("finding invoice for number: {}", invoiceNumber);
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @GetMapping("/specific-fields/{invoiceNumber}")
    public Invoice findSpecificFields(@PathVariable String invoiceNumber) {
        log.info("finding specific fields for invoice number: {}", invoiceNumber);
        Invoice invoice = invoiceRepository.findSpecificFields(invoiceNumber);
        log.info("found invoice with specific fields: {}", invoice);
        return invoice;
    }

    @GetMapping("/by-oder-status/{orderStatus}")
    public List<Order> findOrdersByStatus(@PathVariable String orderStatus) {
        List<Invoice> invoices = invoiceRepository.findByOrdersOrderStatus(orderStatus);
        List<Order> orders = invoices.stream().map(Invoice::getOrders)
                .flatMap(Collection::stream)
                .filter(o -> orderStatus.equals(o.getOrderStatus()))
                .collect(Collectors.toList());
        log.info("Found {} orders with status: {}", orders.size(), orderStatus);
        return orders;
    }

    @GetMapping("/in-city/{city}")
    public List<Invoice> getInvoicesFromCity(@PathVariable String city) {
        List<Invoice> invoices = invoiceRepository.findByShippingAddressCity(city);
        log.info("Found {} invoices from {}", invoices.size(), city);
        return invoices;
    }

    @PutMapping("/{invoiceNumber}")
    public Invoice updateInvoiceName(@PathVariable String invoiceNumber,
                                     @RequestParam("invoiceName") String invoiceName) {
        Invoice invoice = customInvoiceRepository.updateInvoice(invoiceNumber, invoiceName);
        log.info("invoice is updated: {}", invoice);

        return invoice;
    }
}
