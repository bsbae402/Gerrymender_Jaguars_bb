package jaguars.algorithm;

import jaguars.map.district.DistrictManager;
import jaguars.map.precinct.Precinct;
import jaguars.map.precinct.PrecinctManager;
import jaguars.map.state.State;
import jaguars.map.state.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Random;

@Service
public class Algorithm {
    @Autowired
    private StateManager sm;

    @Autowired
    private DistrictManager dm;

    @Autowired
    private PrecinctManager pm;

    @Autowired
    private HttpSession httpSession;

    private Precinct getRandomPrecinct(ArrayList<Precinct> borderPrecincts) {
        Random rand = new Random();
        int idx = rand.nextInt(borderPrecincts.size());
        return borderPrecincts.get(idx);
    }

    private generateNewDistrictBoundaries(Precinct targetPrecinct, State oldDistrictState) {
//      State newDistrictState = sm.cloneState(oldDistrictState)
//      for(District d : newDistrictState.getDistricts()) {
//          for(Precinct p : d.getPrecincts()) {
//              if(p.getCode().equals(targetPrecinct.getCode()) {
//
//              }
//          }
//      }
    }

    public void mainLogic() {
        State initialState = sm.getSessionsState();



    }
}
