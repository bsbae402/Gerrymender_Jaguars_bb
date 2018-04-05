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
public class VotingDataPrecinctController {
    @Autowired
    private VotingDataPrecinctManager vdpm;

    @RequestMapping(value = "votingdataprecinct/get/list", method = RequestMethod.GET)
    public String getAllVotindDataPrecincts() {
        List<VotingDataPrecinct> vdpl = vdpm.getAllVotingDataPreincts();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(vdpl);
    }

    @RequestMapping(value = "/votingdataprecinct/get/byprecinctid", method = RequestMethod.POST)
    public String getVotingDataPrecinctByPrecinctId(@RequestParam("precinct_id") int precinctId) {
        List<VotingDataPrecinct> vdpl = vdpm.getVotingDataPrecinctListByPrecinctId(precinctId);
        if(vdpl == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(vdpl);
    }
}
