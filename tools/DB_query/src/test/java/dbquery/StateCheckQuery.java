package dbquery;

import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.PrecinctNeighborManager;
import jaguarsdbtools.data.PrecinctNeighborRelation;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
import jaguarsdbtools.util.MeasureCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class StateCheckQuery {
    @Autowired
    private StateManager sm;
    @Autowired
    private PrecinctNeighborManager pnm;

    @Test
    public void checkDistrictConnectedComponents() {
        //int stateId = 3; // WI
        int stateId = 4; // WI 2014
        //int stateId = 1; // OH
        //int stateId = 2; // NH
        State state = sm.getState(stateId);
        HashMap<String, PrecinctNeighborRelation> precNeiRelMap
                = pnm.getPrecinctNeighborRelationMap(state.getCode(), state.getElectionYear());
        // { pgeoid : PrecinctNeighborRelation }
        for(District district : state.getDistricts()) {
            ArrayList<Set<String>> connectedComponents = MeasureCalculator.getConnectedComponentsInDistrict(district, precNeiRelMap);
            System.out.println(district.getCode() + " has how many connected components?");
            System.out.println(connectedComponents.size());
            // pnm.getNeighborDataOfPGeoId(state.getCode(), state.getElectionYear(), precinctGeoId);
        }
    }
}
