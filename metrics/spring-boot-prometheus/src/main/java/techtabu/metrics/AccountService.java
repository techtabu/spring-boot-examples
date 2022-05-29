package techtabu.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techtabu.metrics.model.Account;

/**
 * @author TechTabu
 */

@Service
@Slf4j
public class AccountService {

    private final DataInitializer dataInitializer;

    public AccountService(DataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }

    public Account getUserAccountsByUserName(String username) {
        Account account = dataInitializer.getAccounts().stream().filter(a -> a.getUserId().equals(username))
                .findFirst()
                .orElse(null);

        if (account != null) {
            log.info("found account: {}", account);
        }

        return account;
    }

    public Account getUserAccountsByAccountNumber(String accountNumber) {
        Account account = dataInitializer.getAccounts().stream().filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);

        if (account != null) {
            log.info("found account: {}", account);
        }
        return account;
    }
}
