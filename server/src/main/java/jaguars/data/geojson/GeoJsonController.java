package jaguars.data.geojson;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeoJsonController {
    @Autowired
    private StateGeoJsonManager sgjm;
    @Autowired
    private DistrictGeoJsonManager dgjm;
    @Autowired
    private PrecinctGeoJsonManager pgjm;

    @RequestMapping(value = "/geojson/state/bystateid", method = RequestMethod.POST)
    public String getStateGeoJsonByStateId(@RequestParam("state_id") int stateId) {
        FeatureCollection sFeatureCollection = sgjm.getStateGeoByStateId(stateId);
        if(sFeatureCollection == null){
            JsonObject retObj = Json.object().add("error","-1");
            return retObj.toString();
        }
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
        String retStr = gson.toJson(sFeatureCollection);
        return retStr;
    }

    @RequestMapping(value = "/geojson/district/bystateid", method = RequestMethod.POST)
    public String getDistrictGeoJsonByStateId(@RequestParam("state_id") int stateId) {
        FeatureCollection dFeatureCollection = dgjm.getGeoByStateId(stateId);
        if(dFeatureCollection == null) {
            JsonObject retObj = Json.object().add("error","-1");
            return retObj.toString();
        }
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
        String retStr = gson.toJson(dFeatureCollection);
        return retStr;
    }

    @RequestMapping(value = "/geojson/precinct/bystateid", method = RequestMethod.POST)
    public String getPrecinctGeoJsonByStateCode(@RequestParam("state_id") int stateId) {
        FeatureCollection pFeatureCollection = pgjm.getPrecinctGeoByStateId(stateId);
        if(pFeatureCollection == null) {
            JsonObject retObj = Json.object().add("error","-1");
            return retObj.toString();
        }
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
        String retStr = gson.toJson(pFeatureCollection);
        return retStr;
    }
}
