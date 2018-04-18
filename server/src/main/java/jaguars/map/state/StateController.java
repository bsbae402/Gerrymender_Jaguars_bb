package jaguars.map.state;

import com.google.gson.*;
import jaguars.AppConstants;
import jaguars.data.vd_state.VotingDataState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
import java.util.Set;

@RestController
public class StateController {
    @Autowired
    private StateManager sm;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @RequestMapping(value = "state/get/first", method = RequestMethod.GET)
    public State getFirstState() {
        List<State> states = sm.getAllStates();
        return states.get(0);
    }

    @RequestMapping(value = "state/get/list", method = RequestMethod.GET)
    public String getDefaultStateList() {
        List<State> states = sm.getOriginalStates();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonArray stateJsonArray = new JsonArray();
        for(State state : states) {
            Set<VotingDataState> stateVotingDataLi = state.getVotingDataStates();
            JsonElement votingDataLiJson = gson.toJsonTree(stateVotingDataLi);
            JsonElement stateJson = gson.toJsonTree(state);
            stateJson.getAsJsonObject().add(AppConstants.JSON_NAME_VOTING_DATA, votingDataLiJson);
            stateJsonArray.add(stateJson);
        }
        return stateJsonArray.toString();
    }

    @RequestMapping(value = "session/set/stateid", method = RequestMethod.POST)
    public String setSessionStateId(@RequestParam("state_id") int stateId){
        int errno = sm.setSessionStateId(stateId);
        JsonObject retObj = new JsonObject();
        if(errno == -1) {
            retObj.addProperty("error", -1);
        } else {
            retObj.addProperty("error", 0);
        }
        return retObj.toString();
    }

    @RequestMapping(value = "session/get/stateid", method = RequestMethod.GET)
    public String getSessionStateId(){
        int stateId = sm.getSessionsStateId();
        JsonObject retObj = new JsonObject();
        retObj.addProperty("state_id", stateId);
        return retObj.toString();
    }
}
