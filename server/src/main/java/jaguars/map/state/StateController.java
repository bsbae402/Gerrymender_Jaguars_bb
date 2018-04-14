package jaguars.map.state;

import com.google.gson.*;
import jaguars.AppConstants;
import jaguars.data.vd_state.VotingDataState;
import jaguars.data.vd_state.VotingDataStateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class StateController {
    @Autowired
    private StateManager sm;
    @Autowired
    private VotingDataStateManager vdsm;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @RequestMapping(value = "state/get/list", method = RequestMethod.POST)
    public String getDefaultStateList(@RequestParam("name") String name, @RequestParam("year") int year) {
        List<State> states = sm.getStatesByNameYear(name, year);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        JsonArray stateJsonArray = new JsonArray();
        for(State state : states) {
            List<VotingDataState> stateVotingDatas = vdsm.getVotingDataStateListByStateId(state.getId());
            JsonElement votingDatasJson = gson.toJsonTree(stateVotingDatas);
            List<Field> fields = Arrays.asList(VotingDataState.class.getDeclaredFields());
            String stateFieldName = null;
            for(Field field : fields) {
                if (field.getType().equals(State.class)) {
                    stateFieldName = field.getName();
                    break;
                }
            }
            if(stateFieldName != null) {
                for (JsonElement je : votingDatasJson.getAsJsonArray()) {
                    je.getAsJsonObject().remove(stateFieldName);
                }
            }
            JsonElement stateJson = gson.toJsonTree(state);
            stateJson.getAsJsonObject().add(AppConstants.JSON_NAME_VOTING_DATA, votingDatasJson);
            stateJsonArray.add(stateJson);
        }
        return stateJsonArray.toString();
    }

    @RequestMapping(value = "session/set/state", method = RequestMethod.POST)
    public String setSessionStateById(@RequestParam("state_id") int stateId){
        State state = sm.getState(stateId);
        sm.setSessionState(state);
        State actualSessionState = sm.getSessionsState();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(actualSessionState);
    }

    @RequestMapping(value = "session/get/state", method = RequestMethod.GET)
    public String getSessionState(){
        State state = sm.getSessionsState();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(state);
    }
}
