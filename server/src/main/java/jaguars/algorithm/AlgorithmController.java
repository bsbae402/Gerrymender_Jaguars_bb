package jaguars.algorithm;

import com.google.gson.*;
import jaguars.AppConstants;
import jaguars.data.NeighborData;
import jaguars.data.PrecinctNeighborManager;
import jaguars.data.global_storage.AlgorithmGlobalStorage;
import jaguars.data.global_storage.AlgorithmInstance;
import jaguars.map.district.District;
import jaguars.map.precinct.Precinct;
import jaguars.map.state.State;
import jaguars.map.state.StateManager;
import jaguars.util.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
public class AlgorithmController {

    @Autowired
    private AlgorithmGlobalStorage ags;
    @Autowired
    private StateManager sm;
    @Autowired
    private Algorithm algorithm;
    @Autowired
    private HttpSession session;
    @Autowired
    private CalculationManager cm;
    @Autowired
    private PrecinctNeighborManager pnm;
    @Autowired
    private PropertiesManager propMan;

    @RequestMapping(value = "/algorithm/start", method = RequestMethod.POST)
    public String startRedistrictAlgorithm(@RequestParam("state_id") int stateId,
                                           @RequestParam("polsby_compactness_weight") double compactnessWeightPP,
                                           @RequestParam("schwartzberg_compactness_weight") double compactnessWeightSch,
                                           @RequestParam("efficiency_weight") double efficiencyWeight,
                                           @RequestParam("population_threshold") double populationThreshold,
                                           @RequestParam("ignore_precinct_geo_ids[]") String[] ignored_p_array,
                                           @RequestParam("ignore_district_geo_ids[]") String[] ignored_d_array,
                                           @RequestParam("heuristic") int heuristic) {
        State stateOrigin = sm.getState(stateId);
        State algoState = sm.cloneState(stateOrigin);
        HashSet<String> ignored_precincts = new HashSet(Arrays.asList(ignored_p_array));
        HashSet<String> ignored_districts = new HashSet(Arrays.asList(ignored_d_array));

        AlgorithmInstance ai = new AlgorithmInstance(stateOrigin, algoState,compactnessWeightPP, compactnessWeightSch,
                efficiencyWeight, populationThreshold, ignored_precincts, ignored_districts, heuristic);
        Integer hashint = ags.registerAlgorithmInstance(ai);

        Set<District> districts = algoState.getDistricts();
        JsonArray initDistCompactList = new JsonArray();
        for(District d : districts) {
            JsonObject distCompact = new JsonObject();
            distCompact.addProperty("district_geoid", d.getGeoId());
            double compactnessMeasurePP = cm.getCompactnessMeasurePP(d);
            double compactnessMeasureSch = cm.getCompactnessMeasureSch(d);
            distCompact.addProperty("compactness_pp", compactnessMeasurePP);
            distCompact.addProperty("compactness_sch", compactnessMeasureSch);
            distCompact.addProperty("objective_score", cm.objectiveFunction(d,compactnessWeightPP,
                    compactnessMeasureSch,efficiencyWeight));
            initDistCompactList.add(distCompact);
        }

        double initStateEffGap = cm.getEfficiencyGap(algoState);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonObject retObj = new JsonObject();
        retObj.add("init_district_compactness_list", initDistCompactList);
        retObj.addProperty("init_state_objective_score", cm.objectiveFunction(algoState, compactnessWeightPP,
                compactnessWeightSch, efficiencyWeight));
        retObj.addProperty("init_state_efficiency_gap", initStateEffGap);
        retObj.addProperty("algorithm_id", hashint);

        return retObj.toString();
    }

    @RequestMapping(value = "/algorithm/update", method = RequestMethod.POST)
    public String updateRedistrictAlgorithm(
            @RequestParam("algorithm_id") int hashint,
            @RequestParam("loop_count") int loopCount) {
        AlgorithmInstance ai = ags.getAlgorithmInstance(hashint);
        ArrayList<AlgorithmAction> algorithmActions = algorithm.mainLogic(loopCount, ai);

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting().create();

        for (AlgorithmAction aa : algorithmActions) {
            if (aa.checkNaN()){
                algorithmActions.remove(aa);
            }
        }

        JsonElement allChangesElem = gson.toJsonTree(algorithmActions);
        JsonObject retObj = new JsonObject();
        retObj.add("all_changes", allChangesElem);

        return retObj.toString();
    }

    @RequestMapping(value = "algorithm/manual/start", method = RequestMethod.POST)
    public String startManualRedistrict(@RequestParam("state_id") int stateId,
                                        @RequestParam("compactness_weight_pp") double compactnessWeightPP,
                                        @RequestParam("compactness_weight_sch") double compactnessWeightSch,
                                        @RequestParam("efficiency_weight") double efficiencyWeight,
                                        @RequestParam("population_threshold") double populationThreshold,
                                        @RequestParam("heuristic") int heuristic) {
        State stateOrigin = sm.getState(stateId);
        State algoState = sm.cloneState(stateOrigin);
        HashSet<String> ignored_precincts = new HashSet(); // empty set. We are not using them
        HashSet<String> ignored_districts = new HashSet();

        AlgorithmInstance ai = new AlgorithmInstance(stateOrigin, algoState,compactnessWeightPP, compactnessWeightSch,
                efficiencyWeight, populationThreshold, ignored_precincts, ignored_districts, heuristic);
        Integer hashint = ags.registerAlgorithmInstance(ai);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonObject retObj = new JsonObject();
        retObj.addProperty("algorithm_id", hashint);

        return retObj.toString();
    }

    @RequestMapping(value = "algorithm/manual/precinct/select", method = RequestMethod.POST)
    public String selectPrecinctManual(@RequestParam("algorithm_id") int hashint,
                                        @RequestParam("precinct_geoid") String precinctGeoId) {
        AlgorithmInstance ai = ags.getAlgorithmInstance(hashint);
        State algoState = ai.getAlgorithmState();
        Precinct targetPrecinct = algoState.getPrecinctByPgeoid(precinctGeoId);
        ArrayList<Precinct> borderPrecincts = algoState.getBorderPrecincts();
        if(!borderPrecincts.contains(targetPrecinct)) {
            JsonObject retObj = new JsonObject();
            retObj.addProperty("error", -1);
            return retObj.toString();
        }

        if(ai.getIgnored_precints().contains(precinctGeoId)) {
            JsonObject retObj = new JsonObject();
            retObj.addProperty("error", -2);
            return retObj.toString();
        }

        // if the target is on border
        ArrayList<NeighborData> neighborDataList = pnm.getNeighborDataOfPGeoId(algoState.getCode(),
                algoState.getElectionYear(), targetPrecinct.getGeoId());
        // find all neighboring precincts
        ArrayList<Precinct> neighbors = algorithm.extractPrecinctsByNeighborDataList(neighborDataList, algoState.getPrecincts());

        District oldAffiliation = targetPrecinct.getDistrict();
        // The list of selectable districts of the target precinct.
        ArrayList<District> selectableDistricts = new ArrayList<>();
        for(Precinct neighbor : neighbors) {
            District neighborsDistrict = neighbor.getDistrict();
            if(!neighborsDistrict.getCode().equals(oldAffiliation.getCode())
                    && !selectableDistricts.contains(neighborsDistrict)
                    && !ai.getIgnored_districts().contains(neighborsDistrict.getGeoId()))
                selectableDistricts.add(neighborsDistrict);
        }
        if(selectableDistricts.size() == 0) {
            // this means that the targetPrecinct is not a border precinct
            System.out.println("There are no selectable districts! -> target precinct is not border!");
            System.out.println("target precinct: " + targetPrecinct.getName());
            return null;
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        ArrayList<String> selectableDgeoids = new ArrayList<>();
        for(District d : selectableDistricts) {
            selectableDgeoids.add(d.getGeoId());
        }
        JsonElement selectableDgeoidList = gson.toJsonTree(selectableDgeoids);

        JsonObject retObj = new JsonObject();
        retObj.add("selectable_districts", selectableDgeoidList);

        return retObj.toString();
    }

    @RequestMapping(value = "algorithm/manual/precinct/update", method = RequestMethod.POST)
    public String manualPrecinctUpdate(@RequestParam("algorithm_id") int hashint,
                                    @RequestParam("precinct_geoid") String precinctGeoId,
                                    @RequestParam("district_geoid") String districtGeoId){
        AlgorithmInstance ai = ags.getAlgorithmInstance(hashint);
        State algoState = ai.getAlgorithmState();
        Precinct targetPrecinct = algoState.getPrecinctByPgeoid(precinctGeoId);
        District oldAffiliation = targetPrecinct.getDistrict();
        District newAffiliation = algoState.getDistrictByDgeoid(districtGeoId);

        ArrayList<NeighborData> neighborDataList = pnm.getNeighborDataOfPGeoId(algoState.getCode(),
                algoState.getElectionYear(), targetPrecinct.getGeoId());
        // find all neighboring precincts
        ArrayList<Precinct> neighbors = algorithm.extractPrecinctsByNeighborDataList(neighborDataList, algoState.getPrecincts());

        // change the affiliation of the cloned target precinct
        algorithm.changePrecinctAffiliationOfState(targetPrecinct, newAffiliation,
                algoState, neighbors, neighborDataList);

        JsonObject retObj = new JsonObject();
        retObj.addProperty("new_district_compactness_pp", cm.getCompactnessMeasurePP(newAffiliation));
        retObj.addProperty("old_district_compactness_pp", cm.getCompactnessMeasurePP(oldAffiliation));
        retObj.addProperty("new_district_compactness_sch", cm.getCompactnessMeasureSch(newAffiliation));
        retObj.addProperty("old_district_compactness_sch", cm.getCompactnessMeasureSch(oldAffiliation));
        retObj.addProperty("state_wide_efficiency_gap", cm.getEfficiencyGap(algoState));

        return retObj.toString();
    }

    @RequestMapping(value = "algorithm/constraints", method = RequestMethod.GET)
    public String returnAlgorithmConstraints(){
        System.out.println("Getting request with algorithm/constraints");

        JsonObject retObj = new JsonObject();
        retObj.addProperty("polsby_compactness_weight", propMan.getDefaultCompactnessWeightPp());
        retObj.addProperty("schwartzberg_compactness_weight", propMan.getDefaultCompactnessWeightSch());
        retObj.addProperty("efficiency_weight", propMan.getDefaultEfficiencyWeight());
        retObj.addProperty("population_threshold", propMan.getDefaultPopulationThreshold());
        retObj.addProperty("loops", propMan.getMaxLoopSteps());

        System.out.println(retObj);

        return retObj.toString();
    }

    @RequestMapping(value = "algorithm/constraints", method = RequestMethod.POST)
    public String updateAlgorithmContrainst(@RequestParam("polsby_compactness_weight") double compactnessWeightPp,
                                            @RequestParam("schwartzberg_compactness_weight") double compactnessWeightSch,
                                            @RequestParam("efficiency_weight") double efficiencyWeight,
                                            @RequestParam("population_threshold") double populationThreshold,
                                            @RequestParam("loops") int maxLoops){
        boolean fileOk = propMan.updateAlgorithmContraints(compactnessWeightPp, compactnessWeightSch,
                efficiencyWeight, populationThreshold, maxLoops);
        JsonObject retObj = new JsonObject();
        retObj.addProperty("ok", fileOk);
        return retObj.toString();
    }
}
