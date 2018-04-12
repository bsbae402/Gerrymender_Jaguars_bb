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
public class PrecinctGeoJsonManager {
    // key is a string called "stateCensus"
    private HashMap<String, FeatureCollection> precinctGeoJsonFeatureCollections;

    @Autowired
    CensusManager cm;

    @Autowired
    private StateRepository sr;

    public PrecinctGeoJsonManager() {
        precinctGeoJsonFeatureCollections = new HashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();

        FileReader fileReader;
        try{
            for(String stateCode : AppConstants.STATE_CODES) {
                for(int censusYear : AppConstants.CENSUS_YEARS) {
                    boolean fileNotExist = false;
                    String geojsonPath = "";
                    String stateCensus = stateCode + censusYear;
                    switch (stateCensus) {
                        case "OH2010":
                            geojsonPath = AppConstants.PATH_PRECINCT_GEOJSON_OH2010;
                            break;
                        case "NH2010":
                            geojsonPath = AppConstants.PATH_PRECINCT_GEOJSON_NH2010;
                            break;
                        case "WI2010":
                            geojsonPath = AppConstants.PATH_DISTRICT_GEOJSON_WI2010;
                            break;
                        default:
                            fileNotExist = true;
                            break;
                    }
                    if(!fileNotExist) {
                        fileReader = new FileReader(geojsonPath);
                        FeatureCollection featureCollection = gson.fromJson(fileReader, FeatureCollection.class);
                        precinctGeoJsonFeatureCollections.put(stateCensus, featureCollection);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FeatureCollection getPrecinctGeoByStateId(int stateId) {
        State state = sr.findOne(stateId);
        if(state == null)
            return null;
        return getPrecinctGeoByState(state);
    }

    public FeatureCollection getPrecinctGeoByState(State state) {
        int censusYear = cm.getCensusYear(state.getElectionYear());
        String stateCensus = state.getCode() + censusYear;
        return precinctGeoJsonFeatureCollections.get(stateCensus);
    }
}
