package dbquery;

import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.data.vd_district.VotingDataDistrict;
import jaguarsdbtools.data.vd_state.VotingDataState;
import jaguarsdbtools.data.vd_state.VotingDataStateManager;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class VotingDataStateQuery {

    @Autowired
    private StateManager sm;
    @Autowired
    private VotingDataStateManager vdsm;

    @Test
    public void createVotingDataStates() {
        // int stateId = 2; // NH
        // int stateId = 1; // NH
        // int stateId = 3; // NH
        // int stateId = 4; // WI 2014
        int stateId = 5; // NH 2014
        State state = sm.getState(stateId);
        int votesDEM = 0;
        int votesREP = 0;
        int votesOTHERS = 0;
        for(District d : state.getDistricts()) {
            for(VotingDataDistrict vdd : d.getVotingDataDistricts()) {
                if(vdd.getPoliticalParty() == PoliticalParty.DEM)
                    votesDEM += vdd.getVotes();
                else if(vdd.getPoliticalParty() == PoliticalParty.REP)
                    votesREP += vdd.getVotes();
                else if(vdd.getPoliticalParty() == PoliticalParty.OTHER)
                    votesOTHERS += vdd.getVotes();
            }
        }
        VotingDataState vdsDem = new VotingDataState(PoliticalParty.DEM, votesDEM, true, state);
        VotingDataState vdsRep = new VotingDataState(PoliticalParty.REP, votesREP, true, state);
        VotingDataState vdsOther = new VotingDataState(PoliticalParty.OTHER, votesOTHERS, true, state);
        vdsm.saveVotingDataState(vdsDem);
        vdsm.saveVotingDataState(vdsRep);
        vdsm.saveVotingDataState(vdsOther);
    }
}
