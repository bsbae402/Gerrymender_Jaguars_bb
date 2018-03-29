package jaguars.user;

import java.util.ArrayList;
import java.util.List;

public class ActiveAccountManager {
    // DB or App?
    public List<ActiveAccount> activeAccounts = new ArrayList<>();

    public List<ActiveAccount> getActiveAccounts() {
        return activeAccounts;
    }

    public void setAccountActive(Account account) {
        ActiveAccount activeAccount = new ActiveAccount(account);
        activeAccounts.add(activeAccount);
    }

}
