package jaguars.sample;

import jaguars.user.MapProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AccountManager {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private HttpSession session;

    HashMap<String, Account> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
    }

    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<Account>(accounts.values());
    }
    public Account getAccount(String accountId) {
        return accountRepository.findOne(accountId);
    }

    public void setActiveAccountSession(Account loginedAccount) {
        session.setAttribute("ActiveAccount", loginedAccount);
    }

    public Account getActiveAccountSession() {
        return (Account) session.getAttribute("ActiveAccount");
    }
}
