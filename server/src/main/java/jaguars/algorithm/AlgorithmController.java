package jaguars.algorithm;

import jaguars.map.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlgorithmController {

    @Autowired
    private Algorithm algorithm;

    @RequestMapping(value = "/algorithm/start", method = RequestMethod.POST)
    public State startRedistrictAlgorithm(@RequestParam("compactness_weight") double compactnessWeight,
                                          @RequestParam("efficiency_weight") double efficiencyWeight,
                                          @RequestParam("population_threshold") double populationThreshold) {

        State updatedState = algorithm.mainLogic();
        return updatedState;
    }

    @RequestMapping(value = "/algorithm/update", method = RequestMethod.POST)
    public State stopRedistrictAlgorithm(@RequestParam("loop_count") double loopCount) {

        return null;
    }


}
