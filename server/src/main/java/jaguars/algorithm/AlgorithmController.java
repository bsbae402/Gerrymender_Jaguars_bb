package jaguars.algorithm;

import com.google.gson.*;
import jaguars.AppConstants;
import jaguars.data.global_storage.AlgorithmGlobalStorage;
import jaguars.data.global_storage.AlgorithmInstance;
import jaguars.map.district.District;
import jaguars.map.state.State;
import jaguars.map.state.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    @RequestMapping(value = "/algorithm/start", method = RequestMethod.POST)
    public String startRedistrictAlgorithm(@RequestParam("state_id") int stateId,
                                           @RequestParam("compactness_weight") double compactnessWeight,
                                           @RequestParam("efficiency_weight") double efficiencyWeight,
                                           @RequestParam("population_threshold") double populationThreshold) {
        State stateOrigin = sm.getState(stateId);
        State algoState = sm.cloneState(stateOrigin);
        AlgorithmInstance ai = new AlgorithmInstance(stateOrigin, algoState,compactnessWeight,
                efficiencyWeight, populationThreshold);
        Integer hashint = ags.registerAlgorithmInstance(ai);

        Set<District> districts = algoState.getDistricts();
        JsonArray initDistCompactList = new JsonArray();
        for(District d : districts) {
            JsonObject distCompact = new JsonObject();
            distCompact.addProperty("district_id", d.getId());
            double compactnessMeasure = cm.getCompactnessMeasure(d);
            distCompact.addProperty("compactness", compactnessMeasure);
            initDistCompactList.add(distCompact);
        }

        double initStateEffGap = cm.getEfficiencyGap(algoState);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonObject retObj = new JsonObject();
        retObj.addProperty("loops", AppConstants.MAX_LOOP_STEPS);
        retObj.add("init_district_compactness_list", initDistCompactList);
        retObj.addProperty("init_state_efficiency_gap", initStateEffGap);
        retObj.addProperty("algorithm_id", hashint);

        return retObj.toString();
    }

    @RequestMapping(value = "/algorithm/update", method = RequestMethod.POST)
    public String updateRedistrictAlgorithm(
            @RequestParam("algorithm_id") int hashint,
            @RequestParam("loop_count") int loopCount) {
        //// TODO: We are not actually using the given weights, it seems
        //// TODO: We are not checking connected components yet
        AlgorithmInstance ai = ags.getAlgorithmInstance(hashint);
        ArrayList<AlgorithmAction> algorithmActions = algorithm.mainLogic(loopCount, ai);

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create();

        JsonElement allChangesElem = gson.toJsonTree(algorithmActions);
        JsonObject retObj = new JsonObject();
        retObj.add("all_changes", allChangesElem);

        return retObj.toString();
    }

    @RequestMapping(value = "algorithm/storage/register", method = RequestMethod.POST)
    public String storageRegister(@RequestParam("state_id") int stateId,
                                  @RequestParam("compactness_weight") double compactnessWeight,
                                  @RequestParam("efficiency_weight") double efficiencyWeight,
                                  @RequestParam("population_threshold") double populationThreshold) {
        State stateOrigin = sm.getState(stateId);
        State algoState = sm.cloneState(stateOrigin);
        AlgorithmInstance ai = new AlgorithmInstance(stateOrigin, algoState,compactnessWeight,
                efficiencyWeight, populationThreshold);
        Integer hashint = ags.registerAlgorithmInstance(ai);
        JsonObject retObj = new JsonObject();
        retObj.addProperty("algorithm_id", hashint);
        return retObj.toString();
    }

    @RequestMapping(value = "algorithm/storage/get", method = RequestMethod.POST)
    public AlgorithmInstance storageGet(@RequestParam("algorithm_id") int hashint) {
        AlgorithmInstance ai = ags.getAlgorithmInstance(hashint);
        return ai;
    }
}
