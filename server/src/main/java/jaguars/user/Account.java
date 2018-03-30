package jaguars.user;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
    @Id
    private String id;
    private String password;
    private String email;
    private UserRole role;

    public Account() {
    }

    public Account(String id, String password, String email, UserRole role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
