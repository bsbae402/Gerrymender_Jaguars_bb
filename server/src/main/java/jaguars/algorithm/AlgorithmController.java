package jaguars.algorithm;

import com.google.gson.*;
import jaguars.AppConstants;
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
import java.util.List;
import java.util.Set;

@RestController
public class AlgorithmController {

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
        State algoState = sm.getState(stateId);
        session.setAttribute("algo_state", algoState);
        session.setAttribute("compactness_weight", compactnessWeight);
        session.setAttribute("efficiency_weight", efficiencyWeight);
        session.setAttribute("population_threshold", populationThreshold);

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

        return retObj.toString();
    }

    @RequestMapping(value = "/algorithm/update", method = RequestMethod.POST)
    public State stopRedistrictAlgorithm(@RequestParam("loop_count") int loopCount) {

        return null;
    }

    class AlgoStateChange {

    }
    @RequestMapping(value = "sample/algorithm/update", method = RequestMethod.POST)
    public ArrayList<AlgoStateChange> updateSample(@RequestParam("loop_count") int loopCount) {

        return null;
    }

    @RequestMapping(value = "/algorithm/test", method = RequestMethod.POST)
    public void testAlgorithmLogic(@RequestParam("state_id") int stateId) {
        int errno = sm.setSessionStateId(stateId);
        algorithm.mainLogic(100);
    }

    @RequestMapping(value = "algorithm/store/session/state", method = RequestMethod.POST)
    public void storeSessionState(@RequestParam("state_id") int stateId) {
         State state = sm.getState(stateId);
         session.setAttribute("algo_state", state);
    }

    @RequestMapping(value = "algorithm/get/session/state", method = RequestMethod.GET)
    public State getSessionState() {
        State state = (State)session.getAttribute("algo_state");
        return state;
    }

}
