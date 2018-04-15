package jaguars.data.vd_state;

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
public class VotingDataStateController {
    @Autowired
    private VotingDataStateManager vdsm;

    @RequestMapping(value = "votingdatastate/get/list", method = RequestMethod.GET)
    public List<VotingDataState> getAllVotindDataStates() {
        return vdsm.getAllVotingDataStates();
    }
}
