package jaguars.data.vd_district;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VotingDataDistrictController {
    @Autowired
    private VotingDataDistrictManager vddm;

    @RequestMapping(value = "votingdatadistrict/get/list", method = RequestMethod.GET)
    public List<VotingDataDistrict> getAllVotingDataDistricts() {
        return vddm.getAllVotingDataDistricts();
    }
}
