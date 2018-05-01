package jaguarsdbtools.data.vd_state;

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
            votingDataStateList.add(vds);
        }
        return votingDataStateList;
    }

    public VotingDataState saveVotingDataState(VotingDataState vds) {
        return vdsr.save(vds);
    }
}
