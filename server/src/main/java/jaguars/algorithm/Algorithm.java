package jaguars.algorithm;

import jaguars.AppConstants;
import jaguars.map.district.District;
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
    private CalculationManager cm;

    @Autowired
    private HttpSession httpSession;

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
                    move target precinct to other adjacent district(s)
                     */
                }
            }
        }
        return newDistrictState;
    }

    public boolean isImproved() {

    }

    public State mainLogic() {
        State oldState = sm.cloneState(sm.getSessionsState());

        int loopSteps = 0;
        while(loopSteps < AppConstants.MAX_LOOP_STEPS) {
            Precinct targetPrecinctOld = getRandomPrecinct(oldState.getBorderPrecincts());
            State newState = generateNewDistrictBoundaries(targetPrecinctOld, oldState);
            Precinct targetPrecinctNew = newState.getPrecinctByPrecinctCode(targetPrecinctOld.getCode());

            District oldDist1 = targetPrecinctOld.getDistrict();
            District newDist1 = newState.getDistrictByDistrictCode(oldDist1.getCode());
            District newDist2 = targetPrecinctNew.getDistrict();
            District oldDist2 = oldState.getDistrictByDistrictCode(newDist2.getCode());
            double[] compactnessMeasuresOld1 = cm.getCompactnessMeasures(oldDist1);
            double[] compactnessMeasuresOld2 = cm.getCompactnessMeasures(oldDist2);
            double[] compactnessMeasuresNew1 = cm.getCompactnessMeasures(newDist1);
            double[] compactnessMeasuresNew2 = cm.getCompactnessMeasures(newDist2);
            // compare old sum and new sum of compactness

            ArrayList<Integer> districtPopultions = new ArrayList<>();
            for(District d : newState.getDistricts())
                districtPopultions.add(d.getPopulation());
            boolean overPopThrs = MeasureCalculator.checkPopulationThreshold(newState.getPopulation(), districtPopultions);
            if(overPopThrs) {
                loopSteps++;
                continue;
            }

            //double oldEfficiencyGap = MeasureCalculator.getEfficiencyGap(oldState)
            //double newEfficiencyGap = MeasureCalculator.getEfficiencyGap(newState)
            //if(oldEfficiencyGap < newEfficiencyGap) {
            //loopSteps++;
            //continue
            //}

            oldState = newState;
            loopSteps = 0;
        }

        return oldState;
    }
}
