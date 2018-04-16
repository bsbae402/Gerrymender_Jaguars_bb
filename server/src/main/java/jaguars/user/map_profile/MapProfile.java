package jaguars.user.map_profile;

import javax.persistence.*;

@Entity
public class MapProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String profileTitle;

    public MapProfile() {
    }

    public MapProfile(String profileTitle) {
        this.profileTitle = profileTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileTitle() {
        return profileTitle;
    }

    public void setProfileTitle(String profileTitle) {
        this.profileTitle = profileTitle;
    }
}
