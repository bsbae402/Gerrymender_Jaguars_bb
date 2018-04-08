package jaguars.user.map_profile;

import jaguars.sample.Account;

import javax.persistence.*;

@Entity
public class MapProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String profileTitle;

    // If we use fetch = FetchType.LAZY, when we try to return a List<MapProfile> as a REST response,
    // we will see error Failed to write HTTP message: org.springframework.http.converter.HttpMessageNotWritableException
    // which is from Jackson JSON because it cannot convert nested object.

    // I have decided to use "unidirectional approach"
    // The reason behind this is to avoid implementation difficulty in bidirectional approach.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public MapProfile() {
    }

    public MapProfile(String profileTitle, Account account) {
        this.profileTitle = profileTitle;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "MapProfile{" +
                "id=" + id +
                ", profileTitle='" + profileTitle + '\'' +
                ", account=" + account +
                '}';
    }
}
