package dbquery;

import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
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
public class DistrictUpdateQuery {
    @Autowired
    private StateManager sm;
    @Autowired
    private DistrictManager dm;

    @Test
    public void updateDistrictsTotalVoteOfState() {
        int stateId = 2;
        State state = sm.getState(stateId);
        Set<District> districts = state.getDistricts();
        for(District d : districts) {
            Set<Precinct> precincts = d.getPrecincts();
            int totalVotes = 0;
            for(Precinct p : precincts)
                totalVotes += p.getTotalVotes();
            d.setTotalVotes(totalVotes);
            District whatsSaved = dm.updateDistrict(d);
            System.out.println(whatsSaved);
        }
    }
}
