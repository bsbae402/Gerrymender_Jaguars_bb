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
        ArrayList<User> users = new ArrayList<User>(um.findUsersByUsername(username));
        if(users.size() < 1) {
            JsonObject retObj = Json.object().add("error", 2)
                    .add("user_id", "")
                    .add("user_type", -1);
            return retObj.toString();
        }
        if(users.size() > 1)
            System.out.println("BUT there seems more than one users of the same name...");

        User user = users.get(0);
        if(!password.equals(user.getPassword())) {
            JsonObject retObj = Json.object().add("error", 1)
                    .add("user_id", "")
                    .add("user_type", -1);
            return retObj.toString();
        }
        um.setSessionState(user);
        JsonObject retObj = Json.object().add("error", 0)
                .add("user_id", user.getId());
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

    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public String logout() {
        int error = 0;

        if (um.getSessionState() != null)
            um.invalidateSessionState();
        else
            error = 1;

        JsonObject retObj = Json.object().add("error", error);
        return retObj.toString();
    }

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("username") String username, @RequestParam("password") String password,
                         @RequestParam("email") String email) {
        ArrayList<User> users = new ArrayList<User>(um.findUsersByUsername(username));
        if(users.size() >= 1) {
            return "{ \"user_id\" : -1 }";
        }
        User createdUser = um.saveUser(username, password, email, UserRole.USER); // ADMIN should be registered through DB directly
        JsonObject retObj = Json.object().add("user_id", createdUser.getId());
        return retObj.toString();
    }

    @RequestMapping(value = "/user/list/{id}", method = RequestMethod.GET)
    public String getUser(@PathVariable int id) {
        User user = um.findUserById(id);
        if(user == null) {
            JsonObject retObj = Json.object().add("error", 1)
                    .add("user_id", -1)
                    .add("username", "")
                    .add("email", "")
                    .add("role", -1);
            return retObj.toString();
        }

        JsonObject retObj = Json.object().add("error", 0)
                .add("user_id", user.getId())
                .add("username", user.getUsername())
                .add("email", user.getEmail());
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

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String getAllUsers() {
        ArrayList<User> userList = um.getAllUsers();
        JsonArray retJsonArr = Json.array();
        for(User a : userList) {
            JsonObject obj = Json.object().add("user_id", a.getId())
                .add("username", a.getUsername())
                .add("email", a.getEmail());
            retJsonArr.add(obj);
        }
        return retJsonArr.toString();
    }
}
