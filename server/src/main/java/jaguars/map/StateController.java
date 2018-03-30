package jaguars.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;

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

    @RequestMapping(value = "states/get", method = RequestMethod.GET)
    public ArrayList<State> getStateList() {
        return sm.getAllStates();
    }

    @RequestMapping(value = "state/get", method = RequestMethod.POST)
    public State getState(@RequestParam("state_name") String stateName, @RequestParam("year") int stateYear) {
        return sm.getStateByNameYear(stateName, stateYear);
    }
}
