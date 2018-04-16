package jaguars.data.geojson;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jaguars.AppConstants;
import jaguars.util.CensusCalculator;
import jaguars.map.state.State;
import jaguars.map.state.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.HashMap;

@Service
public class DistrictGeoJsonManager {
    private HashMap<String, FeatureCollection> districtGeoMap;

    @Autowired
    private StateRepository sr;

    public DistrictGeoJsonManager() {
        districtGeoMap = new HashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();

        for(String stateCode : AppConstants.STATE_CODES) {
            for (int censusYear : AppConstants.CENSUS_YEARS) {
                boolean fileNotExistYet = false;
                String geojsonPath = "";
                String stateCensus = stateCode + censusYear;
                switch (stateCensus) {
                    case "OH2010":
                        geojsonPath = AppConstants.PATH_DISTRICT_GEOJSON_OH2010;
                        break;
                    case "NH2010":
                        geojsonPath = AppConstants.PATH_DISTRICT_GEOJSON_NH2010;
                        break;
                    case "WI2010":
                        geojsonPath = AppConstants.PATH_DISTRICT_GEOJSON_WI2010;
                        break;
                    default:
                        fileNotExistYet = true;
                        break;
                }
                if (!fileNotExistYet) {
                    try {
                        FileReader fileReader = new FileReader(geojsonPath);
                        FeatureCollection featureCollection = gson.fromJson(fileReader, FeatureCollection.class);
                        districtGeoMap.put(stateCensus, featureCollection);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public FeatureCollection getGeoByStateId(int stateId) {
        State state = sr.findOne(stateId);
        if(state == null)
            return null;
        return getGeoByState(state);
    }

    public FeatureCollection getGeoByState(State state) {
        int censusYear = CensusCalculator.getCensusYear(state.getElectionYear());
        String stateCensus = state.getCode() + censusYear;
        return districtGeoMap.get(stateCensus);
    }
}
