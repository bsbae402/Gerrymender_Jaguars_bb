package jaguars.algorithm;

import jaguars.AppConstants;
import jaguars.data.NeighborData;
import jaguars.data.PrecinctNeighborManager;
import jaguars.data.vd_district.VotingDataDistrict;
import jaguars.data.vd_precinct.VotingDataPrecinct;
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

        // get reference of all precincts in the cloned state
        ArrayList<Precinct> clonedPrecincts = new ArrayList<>();
        for(District d : newDistrictState.getDistricts())
            for(Precinct p : d.getPrecincts())
                clonedPrecincts.add(p);

        // find the target precinct of the cloned state
        Precinct clonedTarget = null;
        for(Precinct p : clonedPrecincts) {
            if(targetPrecinct.getCode().equals(p.getCode()))
                clonedTarget = p;
        }
        if(clonedTarget == null) // should not get in here
            return null;

        // get neighbors' geoids
        ArrayList<NeighborData> neighborDataList = pnm.getNeighborDataOfPGeoId(newDistrictState.getCode(),
                newDistrictState.getElectionYear(), clonedTarget.getGeoId());
        // find all neighboring precincts
        ArrayList<Precinct> neighbors = new ArrayList<>();
        for(Precinct p : clonedPrecincts) {
            for(NeighborData nd : neighborDataList) {
                if(p.getGeoId().equals(nd.getToGeoId()))
                    neighbors.add(p);
            }
        }

        District oldAffiliation = clonedTarget.getDistrict();
        // The list of selectable districts of the target precinct.
        ArrayList<District> selectableDistricts = new ArrayList<>();
        for(Precinct neighbor : neighbors) {
            if(!neighbor.getDistrict().getCode().equals(oldAffiliation.getCode())){
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
        oldAffiliation.getPrecincts().remove(clonedTarget);

        // update area and perimeter of the gaining district(new affiliation)
        newAffiliation.setArea(newAffiliation.getArea() + clonedTarget.getArea());
        ArrayList<Precinct> newAffNeighbors = new ArrayList<>();
        for(Precinct neighbor : neighbors) {
            if(neighbor.getDistrict().equals(newAffiliation))
                newAffNeighbors.add(neighbor);
        }
        double contactLengthToNew = 0.0;
        for(Precinct p : newAffNeighbors) {
            for(NeighborData nd : neighborDataList) {
                if(p.getGeoId().equals(nd.getToGeoId()))
                    contactLengthToNew += nd.getContactLength();
            }
        }
        newAffiliation.setPerimeter(newAffiliation.getPerimeter() + clonedTarget.getPerimeter()
            - 2.0 * contactLengthToNew);

        // update area and perimeter of the losing district(old affiliation)
        oldAffiliation.setArea(oldAffiliation.getArea() + clonedTarget.getArea());
        ArrayList<Precinct> oldAffNeighbors = new ArrayList<>();
        for(Precinct neighbor : neighbors) {
            if(neighbor.getDistrict().equals(oldAffiliation))
                oldAffNeighbors.add(neighbor);
        }
        double contactLengthToOld = 0.0;
        for(Precinct p : oldAffNeighbors) {
            for(NeighborData nd : neighborDataList) {
                if(p.getGeoId().equals(nd.getToGeoId()))
                    contactLengthToOld += nd.getContactLength();
            }
        }
        oldAffiliation.setPerimeter(oldAffiliation.getPerimeter() - clonedTarget.getPerimeter()
                + 2.0 * contactLengthToOld);

        // update population and total votes - new Affiliation
        newAffiliation.setPopulation(newAffiliation.getPopulation() + clonedTarget.getPopulation());
        int totalVotingGained = 0;
        for(VotingDataDistrict vdd : newAffiliation.getVotingDataDistricts()) {
            for(VotingDataPrecinct vdp : clonedTarget.getVotingDataPrecincts()) {
                if(vdd.getPoliticalParty() == vdp.getPoliticalParty()) {
                    vdd.setVotes(vdd.getVotes() + vdp.getVotes());
                    totalVotingGained += vdp.getVotes();
                }
            }
        }
        newAffiliation.setTotalVotes(newAffiliation.getTotalVotes() + totalVotingGained);

        // update population and total votes - old Affiliation
        oldAffiliation.setPopulation(oldAffiliation.getPopulation() - clonedTarget.getPopulation());
        int totalVotingLost = 0;
        for(VotingDataDistrict vdd : oldAffiliation.getVotingDataDistricts()) {
            for(VotingDataPrecinct vdp : clonedTarget.getVotingDataPrecincts()) {
                if(vdd.getPoliticalParty() == vdp.getPoliticalParty()) {
                    vdd.setVotes(vdd.getVotes() - vdp.getVotes());
                    totalVotingLost += vdp.getVotes();
                }
            }
        }
        oldAffiliation.setTotalVotes(oldAffiliation.getTotalVotes() - totalVotingLost);

        // update the borderness of neighboring precincts of the target.
        for(Precinct neighbor : neighbors) {
            // if target's affiliation and neighbor's affiliation are different,
            // the neighbor is automatically border.
            if(!neighbor.getDistrict().equals(clonedTarget.getDistrict())) {
                neighbor.setBorder(true);
                continue;
            }

            // here, the target's affiliation and the neighbor's affiliation are same.
            // These neighbors are all in new affiliation of the target.
            // get neighbors of one neighbor
            ArrayList<NeighborData> neighborsNeighborsDataList = pnm.getNeighborDataOfPGeoId(newDistrictState.getCode(),
                    newDistrictState.getElectionYear(), neighbor.getGeoId());
            ArrayList<Precinct> neighborsNeighbors = new ArrayList<>();
            for(Precinct p : clonedPrecincts) {
                for(NeighborData nd : neighborsNeighborsDataList) {
                    if(p.getGeoId().equals(nd.getToGeoId()))
                        neighborsNeighbors.add(p);
                }
            }
            boolean difAffFound = false; // set if different affiliation is found around the neighbor
            for(Precinct p : neighborsNeighbors) {
                if(!p.getDistrict().equals(neighbor.getDistrict())) {
                    neighbor.setBorder(true);
                    difAffFound = true;
                    break;
                }
            }
            if(!difAffFound)
                neighbor.setBorder(false);
        }
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
