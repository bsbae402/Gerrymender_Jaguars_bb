package jaguars.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserManager {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    public UserManager() {}

    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();

        for(User u : userRepository.findAll()) {
            System.out.println(u);
            allUsers.add(u);
        }
        return allUsers;
    }

    public User findUserById(int userId) {
        return userRepository.findOne(userId);
    }

    public List<User> findUsersByUsername(String username) {
        ArrayList<User> userList = (ArrayList<User>) userRepository.findByUsername(username);
        return userList;
    }

    public User saveUser(String username, String password, String email, UserRole role) {
        User newUser = new User(username, password, email, role);
        return userRepository.save(newUser);    // seems like it returned the created entity
    }

    public void setSessionState(User user) {
        System.out.println("setSessionState() call");
        httpSession.setAttribute("User", (Object)user);
    }

    public User getSessionState() {
        System.out.println("getSessionState() call");
        return (User)httpSession.getAttribute("User");
    }

    public void invalidateSessionState() {
        System.out.println("invalidateSessionState() call");
        httpSession.invalidate();
    }


}
