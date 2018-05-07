package dbquery;

import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.data.vd_district.VotingDataDistrict;
import jaguarsdbtools.data.vd_district.VotingDataDistrictManager;
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinct;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class VotingDataDistrictQuery {
    @Autowired
    private StateManager sm;
    @Autowired
    private VotingDataDistrictManager vddm;

    @Test
    public void createVotingDataDistricts() {
        // int stateId = 2; <- NH
        // int stateId = 1; // OH
        int stateId = 3; // WI
        State state = sm.getState(stateId);
        Set<District> districts = state.getDistricts();
        for(District d : districts) {
            int votesDEM = 0;
            int votesREP = 0;
            int votesOTHER = 0;
            for(Precinct p : d.getPrecincts()) {
                Set<VotingDataPrecinct> vdpSet = p.getVotingDataPrecincts();
                for(VotingDataPrecinct vdp : vdpSet) {
                    if(vdp.getPoliticalParty() == PoliticalParty.DEM)
                        votesDEM += vdp.getVotes();
                    else if(vdp.getPoliticalParty() == PoliticalParty.REP)
                        votesREP += vdp.getVotes();
                    else if(vdp.getPoliticalParty() == PoliticalParty.OTHER)
                        votesOTHER += vdp.getVotes();
                }
            }
            VotingDataDistrict vddDem = new VotingDataDistrict(PoliticalParty.DEM, votesDEM, true, d);
            VotingDataDistrict vddRep = new VotingDataDistrict(PoliticalParty.REP, votesREP, true, d);
            VotingDataDistrict vddOther = new VotingDataDistrict(PoliticalParty.OTHER, votesOTHER, true, d);
            VotingDataDistrict savedDem = vddm.saveVotingDataDistrict(vddDem);
            System.out.println(savedDem);
            VotingDataDistrict savedRep = vddm.saveVotingDataDistrict(vddRep);
            System.out.println(savedRep);
            VotingDataDistrict savedOther = vddm.saveVotingDataDistrict(vddOther);
            System.out.println(savedOther);
        }
    }
}
