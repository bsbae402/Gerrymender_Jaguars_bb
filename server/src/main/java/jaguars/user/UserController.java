package jaguars.user;

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

@RestController
public class UserController {
    @Autowired
    UserManager um;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        System.out.println("login() call");
        User user = um.findUser(username);
        if(user == null) {
            JsonObject retObj = Json.object().add("error", 2)
                    .add("user_id", "")
                    .add("user_type", -1);
            return retObj.toString();
        }
        if(!password.equals(user.getPassword())) {
            JsonObject retObj = Json.object().add("error", 1)
                    .add("user_id", "")
                    .add("user_type", -1);
            return retObj.toString();
        }
        System.out.println("user has been found");
        System.out.println(user);
        JsonObject retObj = Json.object().add("error", 0)
                .add("user_id", username);
        switch (user.getRole()) {
            case USER:
                retObj.add("user_type", 1);
                break;
            case ADMIN:
                retObj.add("user_type", 2);
                break;
            default: // should not reach here
                retObj.add("user_type", 0);
                break;
        }

        return retObj.toString();
    }

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("username") String username, @RequestParam("password") String password,
                         @RequestParam("email") String email) {
        System.out.println("signup() call");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        System.out.println("email: " + email);
        User existingUser = um.findUser(username);
        if(existingUser != null) {
            System.out.println("user already exists!");
            return "{ \"user_id\" : -1 }";
        }

        System.out.println("save a new user");
        um.saveUser(username, password, email, UserRole.USER); // ADMIN should be registered through DB directly
        JsonObject retObj = Json.object().add("user_id", username);
        return retObj.toString();
    }

    @RequestMapping(value = "/user/list/{id}", method = RequestMethod.GET)
    public String getUser(@PathVariable String id) {
        System.out.println("getUser() call");
        // access the DB here
        return "{ \"username\" : \"bongsung\", \"email\" : \"bong@sung.com\" }";
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String getAllUsers() {
        System.out.println("getAllUsers() call");
        // access the DB here

        ArrayList<User> userList = um.getAllUsers();
        JsonArray retJsonArr = Json.array();
        for(User a : userList) {
            JsonObject obj = Json.object().add("username", a.getId())
                .add("email", a.getEmail());
            retJsonArr.add(obj);
        }
        return retJsonArr.toString();
    }
}
