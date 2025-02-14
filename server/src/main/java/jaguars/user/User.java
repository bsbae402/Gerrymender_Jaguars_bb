package jaguars.user;


import jaguars.user.map_profile.MapProfile;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private boolean verified;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<MapProfile> mapProfiles;

    public User() {
    }

    public User(String username, String password, String email, UserRole role, boolean verified) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Set<MapProfile> getMapProfiles() {
        return mapProfiles;
    }

    public void setMapProfiles(Set<MapProfile> mapProfiles) {
        this.mapProfiles = mapProfiles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", verified=" + verified +
                '}';
    }
}
