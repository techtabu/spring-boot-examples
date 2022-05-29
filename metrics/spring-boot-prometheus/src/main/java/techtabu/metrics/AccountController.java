package techtabu.metrics;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techtabu.metrics.model.Account;


/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/account/v1")
@Timed
public class AccountController {

    private final AccountService accountService;

    private final Counter byUserNameCounter;

    public AccountController(AccountService accountService,
                             MeterRegistry registry) {
        this.accountService = accountService;
        this.byUserNameCounter = registry.counter("Account_By_Username");
    }

    @GetMapping("/{username}")
    @Timed(value = "account_username", description = "Time to get an account using user name")
    @Timed(extraTags = { "call_by", "username" })
    public Account getAccountByUserName(@PathVariable("username") String username) throws InterruptedException {

        Thread.sleep((long) ((Math.random() * 1500) + 500));
        this.byUserNameCounter.increment();
        return accountService.getUserAccountsByUserName(username);
    }

    @GetMapping("/account-number/{account-number}")
    @Timed(value = "account_by_account_number", description = "Time to get account by account number")
    @Timed(extraTags = { "call_by", "account_number" })
    public Account getAccountByAccountNumber(@PathVariable("account-number") String accountNumber) throws InterruptedException {
        Thread.sleep((long) ((Math.random() * 2000) + 500));
        return accountService.getUserAccountsByAccountNumber(accountNumber);
    }
}
