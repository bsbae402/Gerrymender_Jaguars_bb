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

//    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
//    public String login(HttpServletRequest request) {
//        System.out.println("login() call");
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        System.out.println(parameterMap);
//        String[] usernameArr = parameterMap.get("username");
//        // String[] is used because there can be multiple parameters on the same name, like:
//        // {name=John, name=Joe, name=Mia}
//        System.out.println(usernameArr[0]);
//        // System.out.println(username[1]); <- throw exception if only one username passed
//        return "{ \"user_id\" : 1, \"user_type\" : 2 }";
//    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        System.out.println("login() call");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        return "{ \"user_id\" : 1, \"user_type\" : 2 }";
    }

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("username") String username, @RequestParam("password") String password,
                         @RequestParam("email") String email) {
        System.out.println("signup() call");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        System.out.println("email: " + email);
        return "{ \"user_id\" : 1 }";
    }

    @RequestMapping(value = "/user/list/{id}", method = RequestMethod.GET)
    public String getUser(HttpServletRequest request, @PathVariable String id) {
        System.out.println("getUser() call");
        // access the DB here
        return "{ \"username\" : \"bongsung\", \"email\" : \"bong@sung.com\" }";
    }

    // -- not used for app, delete later
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
//                "message": "No class jaguars.user.Account entity with id aaa exists!",
//                "path": "/accounts/aaa"
//        }
    }
}
