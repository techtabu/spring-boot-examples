package techtabu.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/**
 * @author TechTabu
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    private Long id;
    private String userId;
    private String accountNumber;
    private Boolean defaultAccount;
    private Set<Address> addresses = new HashSet<>();
    private Set<CreditCard> creditCards = new HashSet<>();

    public Account(Long id, String userId, String accountNumber, boolean defaultAccount) {
        this.id = id;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.defaultAccount = defaultAccount;
    }

}

