package jaguars.algorithm;

import jaguars.AppConstants;
import jaguars.data.PrecinctNeighborManager;
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
    @Autowired
    private PrecinctNeighborManager pnm;

    private double compactnessWeight = AppConstants.DEFAULT_COMPACTNESS_WEIGHT;
    private double efficiencyWeight = AppConstants.DEFAULT_EFFICIENCY_WEIGHT;
    private double populationThreshold = AppConstants.DEFAULT_POPULATION_THRESHOLD;

    private Precinct getRandomPrecinct(ArrayList<Precinct> borderPrecincts) {
        Random rand = new Random();
        int idx = rand.nextInt(borderPrecincts.size());
        return borderPrecincts.get(idx);
    }

    public void updateWeights(double compactnessWeight, double efficiencyWeight, double populationThreshold){
        this.compactnessWeight = compactnessWeight;
        this.efficiencyWeight = efficiencyWeight;
        this.populationThreshold = populationThreshold;
    }

    private State generateNewDistrictBoundaries(Precinct targetPrecinct, State oldDistrictState) {
        State newDistrictState = sm.cloneState(oldDistrictState);

        // find the target precinct of the cloned state
        Precinct clonedTarget = null;
        for(District d : newDistrictState.getDistricts()) {
            for(Precinct p : d.getPrecincts()) {
                if(p.getCode().equals(targetPrecinct.getCode()))
                    clonedTarget = p;
            }
        }
        if(clonedTarget == null) // should not get in here
            return null;
        // get neighbors' geoids
        ArrayList<String> neighborGeoids = pnm.getNeighborPGeoIds(newDistrictState.getCode(),
                newDistrictState.getElectionYear(), clonedTarget.getGeoId());
        // find all neighboring precincts
        ArrayList<Precinct> neighbors = new ArrayList<>();
        for(District d : newDistrictState.getDistricts()) {
            for(Precinct p : d.getPrecincts()) {
                for(String neighborGeoid : neighborGeoids) {
                    if(p.getGeoId().equals(neighborGeoid))
                        neighbors.add(p);
                }
            }
        }
        District currentAffiliation = clonedTarget.getDistrict();
        // The list of selectable districts of the target precinct.
        ArrayList<District> selectableDistricts = new ArrayList<>();
        for(Precinct neighbor : neighbors) {
            if(!neighbor.getDistrict().getCode().equals(currentAffiliation.getCode())){
                selectableDistricts.add(neighbor.getDistrict());
            }
        }
        if(selectableDistricts.size() == 0)
            return null; // this means that the targetPrecinct is not a border precinct

        // select one random district from the array list.
        Random rand = new Random();
        int idx = rand.nextInt(selectableDistricts.size()); // size should be bigger than 0
        District newAffiliation = selectableDistricts.get(idx);
        // change the affiliation of the cloned target precinct
        clonedTarget.setDistrict(newAffiliation);
        return newDistrictState;
    }

    public ArrayList<Precinct> mainLogic(int iterations) {
        ArrayList<Precinct> changedPrecincts = new ArrayList<>();
        int sessionStateId = sm.getSessionsStateId();
        State oldState = sm.cloneState(sm.getState(sessionStateId));
        int loopSteps = 0;
        while(loopSteps < iterations) {
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
            changedPrecincts.add(newState.getPrecinctByPrecinctCode(targetPrecinctOld.getCode()));
            oldState = newState;
            loopSteps = 0;
        }
        return changedPrecincts;
    }
}
