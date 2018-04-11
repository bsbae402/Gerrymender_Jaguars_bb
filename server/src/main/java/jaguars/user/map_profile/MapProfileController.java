package jaguars.user.map_profile;

import jaguars.sample.Account;
import jaguars.sample.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapProfileController {
    @Autowired
    MapProfileManager mpm;
    @Autowired
    AccountManager am;

    @RequestMapping(value = "/mapprofile/list", method = RequestMethod.GET)
    public List<MapProfile> getAllMapProfiles() {
        System.out.println("getAllMapProfiles() call");
        return mpm.getAllMapProfiles();
    }

    // test mapping
    @RequestMapping(value = "/mapprofile/addto/account", method = RequestMethod.POST)
    public MapProfile addMapProfileToAccount(@RequestParam("account_id") String accountId,
                                @RequestParam("profile_title") String profileTitle) {
        System.out.println("addMapProfileToAccount() call");
        Account targetAccount = am.getAccount(accountId);
        MapProfile mapProfile = mpm.saveMapProfile(profileTitle, targetAccount);
        return mapProfile;
    }

    @RequestMapping(value = "/mapprofile/list/account", method = RequestMethod.POST)
    public List<MapProfile> getMapProfilesByAccountId(@RequestParam("account_id") String accountId) {
        System.out.println("getMapProfilesByAccountId() call");
        Account targetAccount = am.getAccount(accountId);
        return mpm.getMapProfilesByAccount(targetAccount);
    }

}
