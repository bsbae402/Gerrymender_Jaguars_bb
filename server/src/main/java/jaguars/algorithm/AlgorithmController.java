package jaguars.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlgorithmController {

    @Autowired
    private Algorithm algorithm;

    @RequestMapping(value = "/algorithm/start", method = RequestMethod.GET)
    public String startRedistrictAlgorithm() {
        algorithm.mainLogic();

        return "";
    }

}
