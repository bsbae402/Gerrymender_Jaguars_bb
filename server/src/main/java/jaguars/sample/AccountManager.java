package jaguars.sample;

import jaguars.user.MapProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AccountManager {
    @Autowired
    AccountRepository accountRepository;

    HashMap<String, Account> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
//        Account bongsung = new Account("bong", "sung123", "bs@gmail.com");
//        Account roger = new Account("roger", "654321", "roger@abc.com");
//        Account dummy1 = new Account("dummy1", "asdf", "asdf@asdf.com");
//        Account dummy2 = new Account("dummy2", "qwer", "qwer@qwer.com");
//        accounts.put(bongsung.getId(), bongsung);
//        accounts.put(roger.getId(), roger);
//        accounts.put(dummy1.getId(), dummy1);
//        accounts.put(dummy2.getId(), dummy2);
    }

    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<Account>(accounts.values());
    }
    public Account getAccount(String accountId) {
        return accountRepository.findOne(accountId);
    }
}
