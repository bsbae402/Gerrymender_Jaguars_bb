package jaguars.data;

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
public class VotingDataStateController {
    @Autowired
    private VotingDataStateManager vdsm;

    @RequestMapping(value = "votingdatastate/get/list", method = RequestMethod.GET)
    public String getAllVotindDataStates() {
        List<VotingDataState> vdsl = vdsm.getAllVotingDataStates();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(vdsl);
    }

    @RequestMapping(value = "/votingdatastate/get/bystateid", method = RequestMethod.POST)
    public String getVotingDataStateByStateId(@RequestParam("state_id") int stateId) {
        List<VotingDataState> vdsl = vdsm.getVotingDataStateListByStateId(stateId);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(vdsl);
    }
}
