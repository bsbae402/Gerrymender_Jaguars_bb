package jaguars.data.vd_state;

import jaguars.map.state.State;
import jaguars.map.state.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDataStateManager {
    @Autowired
    private VotingDataStateRepository vdsr;

    public List<VotingDataState> getAllVotingDataStates() {
        List<VotingDataState> votingDataStateList = new ArrayList<>();
        for(VotingDataState vds : vdsr.findAll()) {
            System.out.println(vds);
            votingDataStateList.add(vds);
        }
        return votingDataStateList;
    }
}
