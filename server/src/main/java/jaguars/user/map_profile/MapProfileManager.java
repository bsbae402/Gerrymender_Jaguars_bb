package jaguars.user.map_profile;

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

}
