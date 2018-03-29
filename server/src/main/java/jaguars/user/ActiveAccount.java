package jaguars.user;

import jaguars.map.MapProfile;

// Inheritance or HAS-A ??
public class ActiveAccount {
    private Account account;
    private MapProfile workingMapProfile;

    public ActiveAccount(Account account) {
        this.account = account;
        workingMapProfile = null;
    }
}
