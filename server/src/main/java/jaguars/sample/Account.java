package jaguars.sample;


import jaguars.user.MapProfile;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    private String id;
    private String password;
    private String email;

    @OneToMany
    private List<MapProfile> mapProfiles = new ArrayList<>();

    public Account() {
    }

    public Account(String id, String password, String email, List<MapProfile> mapProfiles) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.mapProfiles = mapProfiles;
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

    public List<MapProfile> getMapProfiles() {
        return mapProfiles;
    }

    public void setMapProfiles(List<MapProfile> mapProfiles) {
        this.mapProfiles = mapProfiles;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", mapProfiles=" + mapProfiles.toString() +
                '}';
    }
}
