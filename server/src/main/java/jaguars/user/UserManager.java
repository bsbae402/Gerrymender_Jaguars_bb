package jaguars.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserManager {
    HashMap<String, User> users; // we may not use this.

    @Autowired
    UserRepository userRepository;

    public UserManager() {
        // this is temporary...
        users = new HashMap<>();
//        User bongsung = new User("bong", "sung123", "bs@gmail.com");
//        User roger = new User("roger", "654321", "roger@abc.com");
//        User dummy1 = new User("dummy1", "asdf", "asdf@asdf.com");
//        User dummy2 = new User("dummy2", "qwer", "qwer@qwer.com");
//        users.put(bongsung.getId(), bongsung);
//        users.put(roger.getId(), roger);
//        users.put(dummy1.getId(), dummy1);
//        users.put(dummy2.getId(), dummy2);
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();
        for(User u : userRepository.findAll()) {
            System.out.println(u);
            allUsers.add(u);
        }
        return allUsers;
    }

    public User findUser(String id) {
        return userRepository.findOne(id);
    }

    public void saveUser(String id, String password, String email, UserRole role) {
        User newUser = new User(id, password, email, role);
        userRepository.save(newUser);
    }
}
