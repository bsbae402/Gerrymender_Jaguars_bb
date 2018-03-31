package jaguars.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MapProfileManager {
    @Autowired
    MapProfileRepository mpr;

    public ArrayList<MapProfile> getAllMapProfiles() {
        ArrayList<MapProfile> mapProfiles = new ArrayList<>();
        for(MapProfile mp : mpr.findAll()) {
            System.out.println(mp);
            mapProfiles.add(mp);
        }
        return mapProfiles;
    }


}
