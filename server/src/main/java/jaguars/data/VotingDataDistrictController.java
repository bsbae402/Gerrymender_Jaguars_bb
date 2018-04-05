package jaguars.data;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VotingDataDistrictController {
    @Autowired
    private VotingDataDistrictManager vddm;

    @RequestMapping(value = "votingdatadistrict/get/list", method = RequestMethod.GET)
    public String getAllVotingDataDistricts() {
        List<VotingDataDistrict> vddl = vddm.getAllVotingDataDistricts();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(vddl);
    }

    @RequestMapping(value = "/votingdatadistrict/get/bydistrictid", method = RequestMethod.POST)
    public String getVotingDataDistrictsByStateId(@RequestParam("district_id") int districtId) {
        List<VotingDataDistrict> vddl = vddm.getVotingDataDistrictListByDistrictId(districtId);
        if(vddl == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(vddl);
    }
}
