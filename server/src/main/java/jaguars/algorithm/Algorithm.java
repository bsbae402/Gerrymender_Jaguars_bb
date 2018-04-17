package jaguars.algorithm;

import jaguars.AppConstants;
import jaguars.map.district.District;
import jaguars.map.precinct.Precinct;
import jaguars.map.state.State;
import jaguars.map.state.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
public class Algorithm {
    @Autowired
    private StateManager sm;
    @Autowired
    private CalculationManager cm;

    private Precinct getRandomPrecinct(ArrayList<Precinct> borderPrecincts) {
        Random rand = new Random();
        int idx = rand.nextInt(borderPrecincts.size());
        return borderPrecincts.get(idx);
    }

    private State generateNewDistrictBoundaries(Precinct targetPrecinct, State oldDistrictState) {
        State newDistrictState = sm.cloneState(oldDistrictState);

        for(District d : newDistrictState.getDistricts()) {
            for(Precinct p : d.getPrecincts()) {
                if(p.getCode().equals(targetPrecinct.getCode())) {
                    /*
                    1. Randomly move it to a neighboring district if more than one neighbor
                    2. Update Border Precincts
                    3. Adjust metadata for overlying structures (update district population,
                        voting info, area, etc.)
                     */
                }
            }
        }
        return newDistrictState;
    }

    public State mainLogic() {
        int sessionStateId = sm.getSessionsStateId();
        State oldState = sm.cloneState(sm.getState(sessionStateId));
        int loopSteps = 0;
        while(loopSteps < AppConstants.MAX_LOOP_STEPS) {
            Precinct targetPrecinctOld = getRandomPrecinct(oldState.getBorderPrecincts());
            State newState = generateNewDistrictBoundaries(targetPrecinctOld, oldState);
            if(cm.getPopulationThres(newState)) {
                loopSteps++;
                continue;
            }
            double oldScore = cm.objectiveFunction(oldState);
            double newScore = cm.objectiveFunction(newState);
            if (newScore < oldScore){
                loopSteps++;
                continue;
            }
            oldState = newState;
            loopSteps = 0;
        }
        return oldState;
    }
}
