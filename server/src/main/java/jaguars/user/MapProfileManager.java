package jaguars.user;

import jaguars.sample.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapProfileManager {
    @Autowired
    MapProfileRepository mpr;

    public List<MapProfile> getAllMapProfiles() {
        List<MapProfile> mapProfiles = new ArrayList<>();
        for(MapProfile mp : mpr.findAll()) {
            System.out.println(mp);
            mapProfiles.add(mp);
        }
        return mapProfiles;
    }

    public MapProfile saveMapProfile(String profileTitle, Account targetAccount) {
        MapProfile createdProfile = mpr.save(new MapProfile(profileTitle, targetAccount));
        System.out.println("created map profile: ");
        System.out.println(createdProfile);
        return createdProfile;
    }

    public List<MapProfile> getMapProfilesByAccount(Account targetAccount) {
        return mpr.findByAccount(targetAccount);
    }

}
