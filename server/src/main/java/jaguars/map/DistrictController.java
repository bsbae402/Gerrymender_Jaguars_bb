package jaguars.map;

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
    public String getAllDistricts() {
        System.out.println("getAllDistricts() call");
        List<District> districts = dm.getAllDistricts();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(districts);
    }

    @RequestMapping(value = "district/get/bystateid", method = RequestMethod.POST)
    public String getDistrictListByStateId(@RequestParam("state_id") int stateId) {
        System.out.println("getDistrictListByStateId() call");
        List<District> districtList = dm.getDistrictListByStateId(stateId);
        if(districtList == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(districtList);
    }

    @RequestMapping(value = "district/get/byid", method = RequestMethod.POST)
    public String getDistrictById(@RequestParam("district_id") int districtId) {
        System.out.println("getDistrictById() call");
        District district = dm.getDistrictById(districtId);
        if(district == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(district);
    }
}
