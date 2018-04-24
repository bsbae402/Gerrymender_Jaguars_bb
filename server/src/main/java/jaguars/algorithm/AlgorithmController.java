package jaguars.algorithm;

import jaguars.map.state.State;
import jaguars.map.state.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlgorithmController {

    @Autowired
    private StateManager sm;
    @Autowired
    private Algorithm algorithm;

    @RequestMapping(value = "/algorithm/start", method = RequestMethod.POST)
    public String startRedistrictAlgorithm(@RequestParam("compactness_weight") double compactnessWeight,
                                          @RequestParam("efficiency_weight") double efficiencyWeight,
                                          @RequestParam("population_threshold") double populationThreshold) {
        algorithm.updateWeights(compactnessWeight, efficiencyWeight, populationThreshold);
        return "";
    }

    @RequestMapping(value = "/algorithm/update", method = RequestMethod.POST)
    public State stopRedistrictAlgorithm(@RequestParam("loop_count") double loopCount) {

        return null;
    }

    @RequestMapping(value = "/algorithm/test", method = RequestMethod.POST)
    public void testAlgorithmLogic(@RequestParam("state_id") int stateId) {
        int errno = sm.setSessionStateId(stateId);
        algorithm.mainLogic(100);
    }

}
