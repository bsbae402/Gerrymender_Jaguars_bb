package jaguars.map.state;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    @RequestMapping(value = "state/get/list", method = RequestMethod.GET)
    public String getStateList() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(sm.getAllStates());
    }

    @RequestMapping(value = "state/get/name/year", method = RequestMethod.POST)
    public String getStateByNameYear(@RequestParam("name") String name, @RequestParam("year") int year) {
        List<State> foundStates = sm.getStatesByNameYear(name, year);
        if(foundStates.size() < 1){
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        if(foundStates.size() > 1)
            System.out.println("Multiple states found for given name and year!!!");

        State firstOne = foundStates.get(0);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(firstOne);
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
    public String getSessionStateById(){
        State state = sm.getSessionsState();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(state);
    }
}
