package jaguars.data;

import jaguars.map.Precinct;
import jaguars.map.PrecinctRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDataPrecinctManager {
    @Autowired
    private VotingDataPrecinctRepository vdpr;

    @Autowired
    private PrecinctRepository precinctRepository;

    public List<VotingDataPrecinct> getAllVotingDataPreincts() {
        List<VotingDataPrecinct> votingDataPrecinctList = new ArrayList<>();
        for(VotingDataPrecinct vdp : vdpr.findAll()) {
            System.out.println(vdp);
            votingDataPrecinctList.add(vdp);
        }
        return votingDataPrecinctList;
    }

    public List<VotingDataPrecinct> getVotingDataPrecinctListByPrecinctId(int precinctId) {
        Precinct precinct = precinctRepository.findOne(precinctId);
        if(precinct == null)
            return null;
        return vdpr.findByPrecinct(precinct);
    }

}
