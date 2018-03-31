package jaguars.sample;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
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

    @RequestMapping(value="/account/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
    public void deleteAccount(@PathVariable String id) {
        System.out.println("deleteAccount() call");
        // accountRepository.findOne(id) returns null if it can't find an entity with the id
        System.out.println("response: " + accountRepository.findOne(id));
        // void CrudRepository.delete()
        accountRepository.delete(id);
        // if there is no entity with the id, the response body is like:
//        {
//            "timestamp": 1521566544622,
//                "status": 500,
//                "error": "Internal Server Error",
//                "exception": "org.springframework.dao.EmptyResultDataAccessException",
//                "message": "No class jaguars.sample.Account entity with id aaa exists!",
//                "path": "/accounts/aaa"
//        }
    }
}
