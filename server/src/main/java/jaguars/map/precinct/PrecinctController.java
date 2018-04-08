package jaguars.map.precinct;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jaguars.map.district.DistrictManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PrecinctController {
    @Autowired
    PrecinctManager pm;

    @Autowired
    DistrictManager dm;

    @RequestMapping(value = "precinct/get/list", method = RequestMethod.GET)
    public String getAllPrecincts() {
        List<Precinct> precincts = pm.getAllPrecincts();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(precincts);
    }

    @RequestMapping(value = "precinct/get/bydistrictid", method = RequestMethod.POST)
    public String getPrecinctListByDistrictId(@RequestParam("district_id") int districtId) {
        List<Precinct> precinctList = pm.getPrecinctListByDistrictId(districtId);
        if(precinctList == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(precinctList);
    }

    @RequestMapping(value = "precinct/get/byid", method = RequestMethod.POST)
    public String getPrecinctById(@RequestParam("precinct_id") int precinctId) {
        Precinct precinct = pm.getPrecinctById(precinctId);
        if(precinct == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(precinct);
    }

    @RequestMapping(value = "borderprecinct/get/bydistrictid", method = RequestMethod.POST)
    public String getBorderPrecinctsOfDistrict(@RequestParam("district_id") int districtId) {
        List<Precinct> borderPrecinctsOfDistrict = pm.getBorderPrecinctsOfDistrict(districtId);
        // in case of not existing district id
        if(borderPrecinctsOfDistrict == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(borderPrecinctsOfDistrict);
    }

    @RequestMapping(value = "borderprecinct/get/bystateid", method = RequestMethod.POST)
    public String getBorderPrecinctsOfState(@RequestParam("state_id") int stateId) {
        List<Precinct> borderPrecinctsOfState = pm.getBorderPrecinctsOfState(stateId);
        // in case of not existing state id
        if(borderPrecinctsOfState == null) {
            JsonObject retObj = Json.object().add("error", -1);
            return retObj.toString();
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(borderPrecinctsOfState);
    }
}
