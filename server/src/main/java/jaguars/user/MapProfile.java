package jaguars.user;

import jaguars.sample.Account;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MapProfile {
    @Id
    private int id;

    @ManyToOne
    private Account account;

    public MapProfile() {
    }

    public MapProfile(int id, Account account) {
        this.id = id;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                ", account=" + account +
                '}';
    }
}
