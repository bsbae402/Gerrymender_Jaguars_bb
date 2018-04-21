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
    private HashMap<String, HashMap<String, ArrayList<String>>> stateCensusToNeighborTable;

    public PrecinctNeighborManager() {
        stateCensusToNeighborTable = new HashMap<>();

        for(String stateCode : AppConstants.STATE_CODES) {
            for (int censusYear : AppConstants.CENSUS_YEARS) {
                boolean fileNotExistYet = false;
                String jsonPath = "";
                String stateCensus = stateCode + censusYear;
                switch (stateCensus) {
                    case "NH2010":
                        jsonPath = AppConstants.PATH_PRECINCT_NEIGHBORS_NH2010;
                        break;
                    default:
                        fileNotExistYet = true;
                        break;
                }
                if (!fileNotExistYet) {
                    try {
                        HashMap<String, ArrayList<String>> precinctNeighborTable = new HashMap<>();
                        FileReader fileReader = new FileReader(jsonPath);
                        JsonArray neighborRelations = Json.parse(fileReader).asArray();
                        for(int i = 0; i < neighborRelations.size(); i++){
                            JsonObject relation = neighborRelations.get(i).asObject();
                            String from = relation.get("from").asString();
                            JsonArray toJA = relation.get("to").asArray();
                            ArrayList<String> to = new ArrayList<>();
                            for(JsonValue pgeoidJV : toJA.values()) {
                                to.add(pgeoidJV.asString());
                            }
                            precinctNeighborTable.put(from, to);
                            System.out.println(relation.toString());
                        }
                        stateCensusToNeighborTable.put(stateCensus, precinctNeighborTable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public ArrayList<String> getNeighborPGeoIds(String stateCode, int electionYear, String precinctGeoId){
        int censusYear = CensusCalculator.getCensusYear(electionYear);
        String stateCensus = stateCode + censusYear;
        HashMap<String, ArrayList<String>> precinctNeighborTable = stateCensusToNeighborTable.get(stateCensus);
        return precinctNeighborTable.get(precinctGeoId);
    }
}
