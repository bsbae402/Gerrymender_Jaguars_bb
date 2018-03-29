package jaguars.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

// We may not need this class. We want to have a request verified first.
@RestController
public class AccountController {
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

    @RequestMapping(value="/accounts", method = RequestMethod.POST)
    public void postPerson(@RequestBody Account account) {
        accountRepository.save(account);
    }

    @RequestMapping(value="/accounts/{id}", method = RequestMethod.GET)
    public Account getPerson(@PathVariable String id) {
        // accountRepository.findOne(id) returns null if it can't find an entity with the id
        System.out.println("response: " + accountRepository.findOne(id));
        return accountRepository.findOne(id);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        return "{ \"user_id\" : 1, \"user_type\" : 2 }";
    }

    @RequestMapping(value="/accounts", method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
    public void deletePerson(@PathVariable String id) {
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
//                "message": "No class jaguars.user.Account entity with id aaa exists!",
//                "path": "/accounts/aaa"
//        }
    }
}
