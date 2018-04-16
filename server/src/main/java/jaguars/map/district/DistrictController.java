package jaguars.map.district;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.*;
import jaguars.AppConstants;
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
public class DistrictController {
    @Autowired
    private DistrictManager dm;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @RequestMapping(value = "district/get/list", method = RequestMethod.GET)
    public List<District> getAllDistricts() {
        return dm.getAllDistricts();
    }

    @RequestMapping(value = "district/get/bystateid", method = RequestMethod.POST)
    public String getDistrictsByStateId(@RequestParam("state_id") int stateId) {
        List<District> districts = dm.getDistricts(stateId);
        if(districts == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonArray districtJsonArray = new JsonArray();
        for(District d : districts) {
            JsonElement districtJson = gson.toJsonTree(d);
            JsonElement votingDataJson = gson.toJsonTree(d.getVotingDataDistricts());
            districtJson.getAsJsonObject().add(AppConstants.JSON_NAME_VOTING_DATA, votingDataJson);
            districtJsonArray.add(districtJson);
        }
        return districtJsonArray.toString();
    }
}
