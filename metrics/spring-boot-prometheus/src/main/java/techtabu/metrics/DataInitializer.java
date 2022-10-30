package techtabu.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtabu.metrics.model.Account;
import techtabu.metrics.model.Address;
import techtabu.metrics.model.CreditCard;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TechTabu
 */

@Slf4j
@Component
@Data
public class DataInitializer {

    ObjectMapper objectMapper = new ObjectMapper();

    List<Address> addresses = new ArrayList<>();
    List<CreditCard> creditCards = new ArrayList<>();
    List<Account> accounts = new ArrayList<>();

    public void populate() throws IOException {
        if (addresses.isEmpty()) {
            addresses = List.of(
                    new Address(1001L, "142 Main Road", "", "Panama City", "USA","FL", 32405,  "SHIPPING", "account-002"),
                    new Address(1002L, "142 Main Road", "", "Panama City", "USA","FL", 32405,  "BILLING", "account-002"),
                    new Address(1003L, "142 Side Road", "", "Lynn Haven", "USA","FL", 32444,  "SHIPPING", "account-001"),
                    new Address(1004L, "142 Side Road", "", "Lynn haven", "USA","FL", 32444,  "BILLING", "account-001"),
                    new Address(1005L, "334 Wales Dr", "", "Tallahassee", "USA","FL", 32301,  "SHIPPING", "account-002"),
                    new Address(1006L, "1421 Luther King Road", "", "Jacksonville", "USA","FL", 32227,  "BILLING", "account-001")
            );
            addresses.forEach(a -> log.info("Address: {}", a));
        }

        if (creditCards.isEmpty()) {
            creditCards = List.of(
                    new CreditCard(2001L, "5522-6655-8855-9966", "VISA", "account-002"),
                    new CreditCard(2002L, "5522-6655-8855-9966", "MASTER_CARD", "account-001"),
                    new CreditCard(2002L, "5522-4455-6534-7865", "AMERICAN_EXPRESS", "account-002"),
                    new CreditCard(2002L, "4325-3245-2345-6547", "VISA","account-001")
            );
        }

        if (accounts.isEmpty()) {
            accounts = List.of(
                    new Account(1001L, "user-001", "account-001", true),
                    new Account(1002L, "user-002", "account-002", true),
                    new Account(1003L, "user-003", "account-003", true),
                    new Account(1004L, "user-004", "account-004", true)

            );
            accounts.forEach( a -> {
                a.getAddresses().addAll(addresses.stream()
                        .filter(ad -> ad.getAccountNumber().equals(a.getAccountNumber()))
                        .collect(Collectors.toList()));

                a.getCreditCards().addAll(creditCards.stream()
                        .filter(cc -> cc.getAccountNumber().equals(a.getAccountNumber()))
                        .collect(Collectors.toList()));
            });
        }

        accounts.forEach(a -> {
            log.info("Account: {}", a);
        });
    }
}
