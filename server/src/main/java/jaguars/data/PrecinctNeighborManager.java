package jaguars.data;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import jaguars.AppConstants;
import jaguars.util.CensusCalculator;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class PrecinctNeighborManager {
    private HashMap<String, HashMap<String, PrecinctNeighborRelation>> stateCensusToNeighborTable;

    public PrecinctNeighborManager() {
        stateCensusToNeighborTable = new HashMap<>();

        for(String stateCode : AppConstants.STATE_CODES) {
            for (int censusYear : AppConstants.CENSUS_YEARS) {
                boolean fileNotExistYet = false;
                String jsonPath = "";
                String stateCensus = stateCode + censusYear;
                switch (stateCensus) {
                    case "NH2010":
                        jsonPath = AppConstants.PATH_PRECINCT_NEIGHBOR_RELATIONS_NH2010;
                        break;
                    case "OH2010":
                        jsonPath = AppConstants.PATH_PRECINCT_NEIGHBOR_RELATIONS_OH2010;
                        break;
                    default:
                        fileNotExistYet = true;
                        break;
                }
                if (!fileNotExistYet) {
                    try {
                        HashMap<String, PrecinctNeighborRelation> precinctNeighborTable = new HashMap<>();

                        FileReader fileReader = new FileReader(jsonPath);
                        JsonArray neighborRelations = Json.parse(fileReader).asArray();
                        for(int i = 0; i < neighborRelations.size(); i++){
                            JsonObject relation = neighborRelations.get(i).asObject();
                            String fromGeoId = relation.get("from").asString();
                            PrecinctNeighborRelation pnr = new PrecinctNeighborRelation(fromGeoId);

                            JsonArray toJA = relation.get("to").asArray();
                            for(JsonValue neighborDataJV : toJA.values()) {
                                JsonObject neighborDataJO = neighborDataJV.asObject();
                                String toGeoId = neighborDataJO.get("geoid").asString();
                                double contactLength = neighborDataJO.get("contact_length").asDouble();
                                NeighborData neighborData = new NeighborData(toGeoId, contactLength);
                                pnr.addNeighborData(neighborData);
                            }
                            precinctNeighborTable.put(fromGeoId, pnr);
                        }
                        stateCensusToNeighborTable.put(stateCensus, precinctNeighborTable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public ArrayList<NeighborData> getNeighborDataOfPGeoId(String stateCode, int electionYear, String precinctGeoId) {
        int censusYear = CensusCalculator.getCensusYear(electionYear);
        String stateCensus = stateCode + censusYear;
        HashMap<String, PrecinctNeighborRelation> precinctNeighborTable = stateCensusToNeighborTable.get(stateCensus);
        PrecinctNeighborRelation pnr = precinctNeighborTable.get(precinctGeoId);
        return pnr.getNeighborDataList();
    }
}

//[
//    {
//        "from": "33003CHAT01",
//        "to": [
//            {
//            "geoid": "33003JACK01",
//            "contact_length": 12417.407126944
//            },
//            {
//            "geoid": "33003BART01",
//            "contact_length": 6533.2640066255
//            },
//            {
//            "geoid": "33003CONW01",
//            "contact_length": 7691.8160915317
//            },
//            {
//            "geoid": "33007BPUR01",
//            "contact_length": 12380.587724614
//            }
//        ]
//    }, ...
//]