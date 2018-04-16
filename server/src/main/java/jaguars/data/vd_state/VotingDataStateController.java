package jaguars.data.vd_state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VotingDataStateController {
    @Autowired
    private VotingDataStateManager vdsm;

    @RequestMapping(value = "votingdatastate/get/list", method = RequestMethod.GET)
    public List<VotingDataState> getAllVotindDataStates() {
        return vdsm.getAllVotingDataStates();
    }
}
