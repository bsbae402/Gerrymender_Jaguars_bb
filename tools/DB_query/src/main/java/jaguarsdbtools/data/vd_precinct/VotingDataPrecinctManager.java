package jaguarsdbtools.data.vd_precinct;

import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDataPrecinctManager {
    @Autowired
    private VotingDataPrecinctRepository vdpr;

    @Autowired
    private PrecinctRepository pr;

    public List<VotingDataPrecinct> getAllVotingDataPreincts() {
        List<VotingDataPrecinct> votingDataPrecinctList = new ArrayList<>();
        for(VotingDataPrecinct vdp : vdpr.findAll()) {
            System.out.println(vdp);
            votingDataPrecinctList.add(vdp);
        }
        return votingDataPrecinctList;
    }

    public List<VotingDataPrecinct> getVotingDataPrecinctsByPrecinctId(int precinctId) {
        Precinct precinct = pr.findOne(precinctId);
        if(precinct == null)
            return null;
        return vdpr.findByPrecinct(precinct);
    }

}
