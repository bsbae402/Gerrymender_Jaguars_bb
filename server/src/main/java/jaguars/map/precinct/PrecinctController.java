package jaguars.map.precinct;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.*;
import jaguars.AppConstants;
import jaguars.data.vd_precinct.VotingDataPrecinct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class PrecinctController {
    @Autowired
    PrecinctManager pm;

    @RequestMapping(value = "precinct/get/list", method = RequestMethod.GET)
    public List<Precinct> getAllPrecincts() {
        return pm.getAllPrecincts();
    }

    @RequestMapping(value = "precinct/get/bydistrictid", method = RequestMethod.POST)
    public String getPrecinctsByStateId(@RequestParam("district_id") int districtId) {
        List<Precinct> precincts = pm.getPrecinctsByDistrictId(districtId);
        if(precincts == null){
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonArray precinctJsonArray = new JsonArray();
        for(Precinct p : precincts) {
            Set<VotingDataPrecinct> votingDataLi = p.getVotingDataPrecincts();
            JsonElement votingDataJson = gson.toJsonTree(votingDataLi);
            JsonElement precinctJson = gson.toJsonTree(p);
            precinctJson.getAsJsonObject().add(AppConstants.JSON_NAME_VOTING_DATA, votingDataJson);
            precinctJsonArray.add(precinctJson);
        }
        return precinctJsonArray.toString();
    }
}
