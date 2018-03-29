package jaguars.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// We may not need this class. We want to have a request verified first.
@RestController
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

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
    public String login(@RequestBody MultiValueMap<String,String> formData) {
        return "{ user_id : 1, user_type : 2 }";
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
