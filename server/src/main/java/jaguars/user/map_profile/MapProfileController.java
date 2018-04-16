package jaguars.user.map_profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapProfileController {
    @Autowired
    private MapProfileManager mpm;

    @RequestMapping(value = "/mapprofile/list", method = RequestMethod.GET)
    public List<MapProfile> getAllMapProfiles() {
        return mpm.getAllMapProfiles();
    }
}
