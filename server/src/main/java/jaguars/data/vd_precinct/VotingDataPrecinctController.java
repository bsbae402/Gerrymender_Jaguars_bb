package jaguars.data.vd_precinct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VotingDataPrecinctController {
    @Autowired
    private VotingDataPrecinctManager vdpm;

    @RequestMapping(value = "votingdataprecinct/get/list", method = RequestMethod.GET)
    public List<VotingDataPrecinct> getAllVotindDataPrecincts() {
        return vdpm.getAllVotingDataPreincts();
    }
}
