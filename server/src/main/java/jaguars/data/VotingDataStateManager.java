package jaguars.data;

import jaguars.map.State;
import jaguars.map.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDataStateManager {
    @Autowired
    private VotingDataStateRepository vdsr;
    @Autowired
    private StateRepository stateRepository;

    public List<VotingDataState> getAllVotingDataStates() {
        List<VotingDataState> votingDataStateList = new ArrayList<>();
        for(VotingDataState vds : vdsr.findAll()) {
            System.out.println(vds);
            votingDataStateList.add(vds);
        }
        return votingDataStateList;
    }

    public List<VotingDataState> getVotingDataStateListByStateId(int stateId) {
        State state = stateRepository.findOne(stateId);
        return vdsr.findByState(state);
    }

}
