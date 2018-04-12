package jaguars.data.geojson;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jaguars.AppConstants;
import jaguars.data.CensusManager;
import jaguars.map.state.State;
import jaguars.map.state.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.HashMap;

@Service
public class StateGeoJsonManager {
    // key is a string called "stateCensus"
    private HashMap<String, FeatureCollection> stateGeoJsonFeatureCollections;

    @Autowired
    private CensusManager cm;

    @Autowired
    private StateRepository sr;

    public StateGeoJsonManager() {
        stateGeoJsonFeatureCollections = new HashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
        FileReader fileReader;
        try{
            for(String stateCode : AppConstants.STATE_CODES) {
                for(int censusYear : AppConstants.CENSUS_YEARS){
                    boolean fileNotExist = false;
                    String geojsonPath = "";
                    String stateCensus = stateCode + censusYear;
                    switch (stateCensus) {
                        case "OH2010":
                            geojsonPath = AppConstants.PATH_STATE_GEOJSON_OH2010;
                            break;
                        case "NH2010":
                            geojsonPath = AppConstants.PATH_STATE_GEOJSON_NH2010;
                            break;
                        case "WI2010":
                            geojsonPath = AppConstants.PATH_STATE_GEOJSON_WI2010;
                            break;
                        default:
                            fileNotExist = true;
                            break;
                    }
                    if(!fileNotExist) {
                        fileReader = new FileReader(geojsonPath);
                        FeatureCollection featureCollection = gson.fromJson(fileReader, FeatureCollection.class);
                        stateGeoJsonFeatureCollections.put(stateCensus, featureCollection);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FeatureCollection getStateGeoByStateId(int stateId) {
        State state = sr.findOne(stateId);
        if(state == null)
            return null;
        return getStateGeoByState(state);
    }

    public FeatureCollection getStateGeoByState(State state) {
        int censusYear = cm.getCensusYear(state.getElectionYear());
        String stateCensus = state.getCode() + censusYear;
        return stateGeoJsonFeatureCollections.get(stateCensus);
    }
}
