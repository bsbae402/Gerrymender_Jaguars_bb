package jaguars.sample;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

// We may not need this class. We want to have a request verified first.
@RestController
public class AccountController {
    @Autowired
    AccountManager am;
    @Autowired
    AccountRepository accountRepository;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @RequestMapping(value="/account", method = RequestMethod.POST)
    public void postAccount(@RequestBody Account account) {
        System.out.println("postAccount() call");
        System.out.println(account);
        accountRepository.save(account);
    }

    @RequestMapping(value="/account/get/{id}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable String id) {
        System.out.println("getAccount() call");
        System.out.println("response: " + accountRepository.findOne(id));
        // accountRepository.findOne(id) returns null if it can't find an entity with the id
        return accountRepository.findOne(id);
    }

    @RequestMapping(value="/account", method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        System.out.println("getAllAccount() call");
        return (List<Account>) accountRepository.findAll();
    }

    @RequestMapping(value = "/account/delete/{id}", method = RequestMethod.DELETE)
    public void deleteAccount(@PathVariable String id) {
        System.out.println("deleteAccount() call");
        // accountRepository.findOne(id) returns null if it can't find an entity with the id
        System.out.println("response: " + accountRepository.findOne(id));
        accountRepository.delete(id);
    }

    @RequestMapping(value = "/account/login", method = RequestMethod.POST)
    public String loginByAccountId(@RequestParam("account_id") String accountId){
        System.out.println("loginByAccountId() call");
        Account foundAccount = accountRepository.findOne(accountId);
        am.setActiveAccountSession(foundAccount);
        JsonObject retObj = Json.object().add("error", 0);
        return retObj.toString();
    }

    @RequestMapping(value = "/account/login/{accountId}", method = RequestMethod.POST)
    public String loginByAccountIdParam(@PathVariable String accountId){
        System.out.println("loginByAccountIdParam() call");
        Account foundAccount = accountRepository.findOne(accountId);
        am.setActiveAccountSession(foundAccount);
        JsonObject retObj = Json.object().add("error", 0);
        return retObj.toString();
    }

    @RequestMapping(value = "account/session/active", method = RequestMethod.GET)
    public String getActiveAccount() {
        System.out.println("getActiveAccount() call");
        Account activeAccount = am.getActiveAccountSession();
        JsonObject retObj = Json.object().add("account_id", activeAccount.getId())
                .add("password", activeAccount.getPassword())
                .add("email", activeAccount.getEmail());
        return retObj.toString();
    }

    @RequestMapping(value = "account/session/{accountId}", method = RequestMethod.GET)
    public String setActiveAccount(@PathVariable String accountId) {
        System.out.println("setActiveAccount() call");
        Account foundAccount = accountRepository.findOne(accountId);
        am.setActiveAccountSession(foundAccount);
        JsonObject retObj = Json.object().add("error", 0);
        return retObj.toString();
    }
}
