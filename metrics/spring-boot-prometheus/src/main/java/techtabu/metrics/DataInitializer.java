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

    @Value("${account.data.files.path}")
    String filesLocation;

    @Value("${account.data.address.filename:address.json}")
    String addressesFileName;

    @Value("${account.data.creditcards.filename:creditcards.json}")
    String creditCardsFileName;

    @Value("${account.data.accounts.filename:accounts.json}")
    String accountsFileName;

    ObjectMapper objectMapper = new ObjectMapper();

    List<Address> addresses = new ArrayList<>();
    List<CreditCard> creditCards = new ArrayList<>();
    List<Account> accounts = new ArrayList<>();

    public void populate() throws IOException {
        if (addresses.isEmpty()) {
            addresses = Arrays.asList(objectMapper.readValue(Paths.get(filesLocation + addressesFileName).toFile(), Address[].class));
            addresses.forEach(a -> log.info("Address: {}", a));
        }

        if (creditCards.isEmpty()) {
            creditCards = Arrays.asList(objectMapper.readValue(Paths.get(filesLocation + creditCardsFileName).toFile(), CreditCard[].class));
        }

        if (accounts.isEmpty()) {
            accounts = Arrays.asList(objectMapper.readValue(Paths.get(filesLocation + accountsFileName).toFile(), Account[].class));
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
