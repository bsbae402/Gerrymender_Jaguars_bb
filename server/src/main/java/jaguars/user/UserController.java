package jaguars.user;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import jaguars.AppConstants;
import jaguars.user.pending_verifications.PendingVerifications;
import jaguars.user.pending_verifications.PendingVerificationsManager;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

@RestController
public class UserController {
    @Autowired
    UserManager um;
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    PendingVerificationsManager pvm;

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
        if(!user.isVerified() || !password.equals(user.getPassword())) {
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
                         @RequestParam("email") String email, @RequestParam("ignore_verify") boolean verify)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ArrayList<User> users = new ArrayList<User>(um.findUsersByUsername(username));

        if(users.size() >= 1) {
            return "{ \"user_id\" : -1 }";
        }

        if (!verify){
            // GENERATE KEY
            String key = Long.toHexString(Double.doubleToLongBits(Math.random()));

            String body = "In order to verify your account, click on the following link:\n" +
                    "http://gerrymandering.online/verify.html?u=" + username + "&v=" + key;

            emailService.sendSimpleMessage(email, "Gerrymandering Online Verification", body);

            // Add Pending Verification
            pvm.addPendingVerification(username, key);
        }
        User createdUser = um.saveUser(username, password, email, UserRole.USER, verify); // ADMIN should be registered through DB directly
        JsonObject retObj = Json.object().add("user_id", createdUser.getId());
        return retObj.toString();
    }

    @RequestMapping(value = "/user/verify", method = RequestMethod.POST)
    public String verify(@RequestParam("username") String username,
                         @RequestParam("verify") String verify) {
        int error = 0;
        try {
            PendingVerifications pv = pvm.findUsersByUsername(username).get(0);


            if (pv.getVerify().equals(verify)) {
                pvm.removePendingVerification(pv.getId());

                User user = um.findUsersByUsername(username).get(0);
                um.verify(user.getId());
            } else {
                error = -1;
            }
        } catch (java.lang.IndexOutOfBoundsException e){
            error = -1;
        }

        JsonObject retObj = Json.object().add("error", error);
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
                    .add("role", -1)
                    .add("verified", false);
            return retObj.toString();
        }

        JsonObject retObj = Json.object().add("error", 0)
                .add("user_id", user.getId())
                .add("username", user.getUsername())
                .add("email", user.getEmail())
                .add("verified", user.isVerified());
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

    /********************************
     *  ADMIN USER FUNCTIONALITY    *
     ********************************/
    @RequestMapping(value = "/admin/user/list", method = RequestMethod.GET)
    public String getAllUsers() {
        //if (um.getSessionState() != null && um.getSessionState().getRole() == UserRole.ADMIN) {
            ArrayList<User> userList = um.getAllUsers();
            JsonArray retJsonArr = Json.array();
            for (User a : userList) {
                JsonObject obj = Json.object().add("user_id", a.getId())
                        .add("username", a.getUsername())
                        .add("password", a.getPassword())
                        .add("email", a.getEmail())
                        .add("role", a.getRole().name());
                retJsonArr.add(obj);
            }
            return retJsonArr.toString();

    }

    @RequestMapping(value = "/admin/user/{userid}/delete", method = RequestMethod.GET)
    public String adminDeleteUser(@PathVariable int userid) {
        //if (um.getSessionState() != null && um.getSessionState().getRole() == UserRole.ADMIN){
        um.removeUser(userid);
        return "{ \"error\" : 0 }";

    }

    @RequestMapping(value = "/admin/user/{userid}", method = RequestMethod.POST)
    public String adminEditUser(@PathVariable int userid, @RequestParam("username") String username, @RequestParam("password") String password,
                                @RequestParam("email") String email) {
        //if (um.getSessionState() != null && um.getSessionState().getRole() == UserRole.ADMIN){
            um.editUser(userid, username, password, email);
            return "{ \"error\" : 0 }";

    }

    @RequestMapping(value = "/admin/user/add", method = RequestMethod.POST)
    public String adminAddUser(@RequestParam("username") String username, @RequestParam("password") String password,
                         @RequestParam("email") String email) {
        // if (um.getSessionState() != null && um.getSessionState().getRole() == UserRole.ADMIN) {
            ArrayList<User> users = new ArrayList<>(um.findUsersByUsername(username));
            if (users.size() >= 1) {
                return "{ \"error\" : 1," +
                        "\"user_id\" : -1 }";
            }
            User createdUser = um.saveUser(username, password, email, UserRole.USER, true); // ADMIN should be registered through DB directly
            JsonObject retObj = Json.object().add("user_id", createdUser.getId());
            return retObj.toString();
    }



}
