package jaguars.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AccountManager {
    HashMap<String, Account> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
        Account bongsung = new Account("bong", "sung123", "bs@gmail.com", UserRole.ADMIN);
        Account roger = new Account("roger", "654321", "roger@abc.com", UserRole.ADMIN);
        Account dummy1 = new Account("dummy1", "asdf", "asdf@asdf.com", UserRole.USER);
        Account dummy2 = new Account("dummy2", "qwer", "qwer@qwer.com", UserRole.USER);
        accounts.put(bongsung.getId(), bongsung);
        accounts.put(roger.getId(), roger);
        accounts.put(dummy1.getId(), dummy1);
        accounts.put(dummy2.getId(), dummy2);
    }

    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<Account>(accounts.values());
    }
}
