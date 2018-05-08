package jaguars.user.map_profile;

import jaguars.map.state.State;
import jaguars.user.User;

import javax.persistence.*;

@Entity
public class MapProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String profileTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private State state;

    public MapProfile() {
    }

    public MapProfile(String profileTitle, User user, State state) {
        this.profileTitle = profileTitle;
        this.user = user;
        this.state = state;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MapProfile{" +
                "id=" + id +
                ", profileTitle='" + profileTitle + '\'' +
                '}';
    }
}
