package jaguars.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MapProfileController {
    @Autowired
    MapProfileManager mpm;

    @RequestMapping(value = "/mapprofile/list", method = RequestMethod.GET)
    public ArrayList<MapProfile> getAllMapProfiles() {
        System.out.println("getAllMapProfiles() call");
        return mpm.getAllMapProfiles();
    }

    // test mapping
    @RequestMapping(value = "/mapprofile/add/account", method = RequestMethod.POST)
    public String addMapProfileToAccount(@RequestParam("account_id") String accountId,
                                @RequestParam("map_profile") MapProfile mapProfile) {
        System.out.println("addMapProfileToAccount() call");
        return "{}";
    }
}
