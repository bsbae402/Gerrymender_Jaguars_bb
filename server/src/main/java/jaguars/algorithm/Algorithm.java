package jaguars.algorithm;

import jaguars.data.NeighborData;
import jaguars.data.PrecinctNeighborManager;
import jaguars.data.PrecinctNeighborRelation;
import jaguars.data.global_storage.AlgorithmInstance;
import jaguars.data.vd_district.VotingDataDistrict;
import jaguars.data.vd_precinct.VotingDataPrecinct;
import jaguars.map.district.District;
import jaguars.map.precinct.Precinct;
import jaguars.map.state.State;
import jaguars.map.state.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Service
public class Algorithm {
    @Autowired
    private StateManager sm;
    @Autowired
    private CalculationManager cm;
    @Autowired
    private PrecinctNeighborManager pnm;

    private Precinct getRandomPrecinct(ArrayList<Precinct> borderPrecincts) {
        Random rand = new Random();
        int idx = rand.nextInt(borderPrecincts.size());
        return borderPrecincts.get(idx);
    }

    // select one random district from the array list.
    private District getRandomDistrict(ArrayList<District> selectableDistricts) {
        Random rand = new Random();
        int idx = rand.nextInt(selectableDistricts.size()); // size should be bigger than 0
        return selectableDistricts.get(idx);
    }

    public boolean renewPrecinctCode(Precinct targetPrecinct) {
        String postfix = targetPrecinct.getCode().substring(4);
        String districtCode = targetPrecinct.getDistrict().getCode();
        targetPrecinct.setCode(districtCode + postfix);
        return true;
    }

    // should call this method after changing the target's affiliation
    private double calculateChangedPerimeter(District district, Precinct targetPrecinct, ArrayList<Precinct> targetNeighbors,
                                             ArrayList<NeighborData> neighborDataList) {
        ArrayList<Precinct> contactsOnDistrictSide = new ArrayList<>();
        for(Precinct neighbor : targetNeighbors) {
            if(neighbor.getDistrict().equals(district))
                contactsOnDistrictSide.add(neighbor);
        }
        double contactLength = 0.0;
        for(Precinct p : contactsOnDistrictSide) {
            for(NeighborData nd : neighborDataList) {
                if(p.getGeoId().equals(nd.getToGeoId()))
                    contactLength += nd.getContactLength();
            }
        }
        // if the given district is the new affiliation of targetPrecinct
        if(district.equals(targetPrecinct.getDistrict())) {
            return district.getPerimeter() + targetPrecinct.getPerimeter() - 2.0 * contactLength;
        }
        // else: the given district is the old affiliation of targetPrecinct
        else {
            return district.getPerimeter() - targetPrecinct.getPerimeter() + 2.0 * contactLength;
        }
    }

    private void updateChangedDistrictVotingData(District changedDistrict, Precinct targetPrecinct) {
        int signFactor = -1; // if the changed district is old affiliation of target precinct

        if(changedDistrict.equals(targetPrecinct.getDistrict()))
            signFactor = 1; // if the changed district is new affiliation of target precinct

        int votingDiff = 0;
        for(VotingDataDistrict vdd : changedDistrict.getVotingDataDistricts()) {
            for(VotingDataPrecinct vdp : targetPrecinct.getVotingDataPrecincts()) {
                if(vdd.getPoliticalParty() == vdp.getPoliticalParty()) {
                    vdd.setVotes(vdd.getVotes() + (signFactor * vdp.getVotes()));
                    votingDiff = votingDiff + (signFactor * vdp.getVotes());
                }
            }
        }
        changedDistrict.setTotalVotes(changedDistrict.getTotalVotes() + votingDiff);
    }

    public ArrayList<Precinct> extractPrecinctsByNeighborDataList(ArrayList<NeighborData> neighborDataList,
                                                                    ArrayList<Precinct> precincts) {
        ArrayList<Precinct> extracted = new ArrayList<>();
        for(Precinct p : precincts) {
            for(NeighborData nd : neighborDataList) {
                if(p.getGeoId().equals(nd.getToGeoId()))
                    extracted.add(p);
            }
        }
        return extracted;
    }

    private State generateNewDistrictBoundaries(Precinct targetPrecinct, State oldDistrictState, AlgorithmInstance ai) {
        State newDistrictState = sm.cloneState(oldDistrictState);

        // get reference of all precincts in the cloned state
        ArrayList<Precinct> clonedPrecincts = newDistrictState.getPrecincts();

        // find the target precinct of the cloned state
        Precinct clonedTarget = null;
        for(Precinct p : clonedPrecincts) {
            if(targetPrecinct.getCode().equals(p.getCode()))
                clonedTarget = p;
        }
        if(clonedTarget == null) {
            System.out.println("clonedTarget is null!!");
            // should not get in here
            return null;
        }

        ArrayList<NeighborData> neighborDataList = pnm.getNeighborDataOfPGeoId(newDistrictState.getCode(),
                newDistrictState.getElectionYear(), clonedTarget.getGeoId());
        // find all neighboring precincts
        ArrayList<Precinct> neighbors = extractPrecinctsByNeighborDataList(neighborDataList, clonedPrecincts);

        District oldAffiliation = clonedTarget.getDistrict();
        // The list of selectable districts of the target precinct.
        ArrayList<District> selectableDistricts = new ArrayList<>();
        for(Precinct neighbor : neighbors) {
            District neighborsDistrict = neighbor.getDistrict();
            if(!neighborsDistrict.getCode().equals(oldAffiliation.getCode())
                    && !selectableDistricts.contains(neighborsDistrict)
                    && !ai.getIgnored_districts().contains(neighborsDistrict.getGeoId()))
                selectableDistricts.add(neighborsDistrict);
        }
        if(selectableDistricts.size() == 0) {
            // this means that the targetPrecinct is not a border precinct
            System.out.println("There are no selectable districts, but the target precinct is border...");
            System.out.println("target precinct name: " + targetPrecinct.getName());
            System.out.println("target precinct geoid: " + targetPrecinct.getGeoId());

            return null;
        }

        District newAffiliation = getRandomDistrict(selectableDistricts);

        // update the state/district/precinct in accordance with the move
        changePrecinctAffiliationOfState(clonedTarget, newAffiliation, newDistrictState, neighbors, neighborDataList);

        return newDistrictState;
    }

    public boolean changePrecinctAffiliationOfState(Precinct clonedTargetPrecinct, District newAffiliation,
                                                    State newState, ArrayList<Precinct> neighbors,
                                                    ArrayList<NeighborData> neighborDataList) {
        District oldAffiliation = clonedTargetPrecinct.getDistrict();

        // change the affiliation of the cloned target precinct
        clonedTargetPrecinct.setDistrict(newAffiliation);
        newAffiliation.getPrecincts().add(clonedTargetPrecinct);
        oldAffiliation.getPrecincts().remove(clonedTargetPrecinct);

        // update precinct code
        boolean codeFormatOkay = renewPrecinctCode(clonedTargetPrecinct);
        if(!codeFormatOkay)
            System.out.println(clonedTargetPrecinct.getCode() + " is wrong code format!");

        // update area and perimeter of the gaining district(new affiliation)
        newAffiliation.setArea(newAffiliation.getArea() + clonedTargetPrecinct.getArea());
        newAffiliation.setPerimeter(calculateChangedPerimeter(newAffiliation, clonedTargetPrecinct,
                neighbors, neighborDataList));

        // update area and perimeter of the losing district(old affiliation)
        oldAffiliation.setArea(oldAffiliation.getArea() - clonedTargetPrecinct.getArea());
        oldAffiliation.setPerimeter(calculateChangedPerimeter(oldAffiliation, clonedTargetPrecinct,
                neighbors, neighborDataList));

        // update population and total votes - new Affiliation
        newAffiliation.setPopulation(newAffiliation.getPopulation() + clonedTargetPrecinct.getPopulation());
        updateChangedDistrictVotingData(newAffiliation, clonedTargetPrecinct);

        // update population and total votes - old Affiliation
        oldAffiliation.setPopulation(oldAffiliation.getPopulation() - clonedTargetPrecinct.getPopulation());
        updateChangedDistrictVotingData(oldAffiliation, clonedTargetPrecinct);

        // [THIS SHOULD NOT BE TRUE IF CONNECTED COMPONENT CONSTRAINT CHECK IS COMPLETE]
        // check if the target is border or not
        // -> if target was previously isolated by the old affiliation, (previously island)
        // it now should not be the border anymore with the new affiliation.
        boolean difAffFound = false;
        for(Precinct nei : neighbors) {
            if(!nei.getDistrict().equals(newAffiliation)) {
                difAffFound = true;
                break;
            }
        }
        clonedTargetPrecinct.setBorder(difAffFound);

        // update the borderness of neighboring precincts of the target.
        for(Precinct neighbor : neighbors) {
            // if target's affiliation and neighbor's affiliation are different,
            // the neighbor is automatically border.
            if(!neighbor.getDistrict().equals(clonedTargetPrecinct.getDistrict())) {
                neighbor.setBorder(true);
                continue;
            }
            // here, the target's affiliation and the neighbor's affiliation are same.
            // These neighbors are all in new affiliation of the target.
            // get neighbors of one neighbor
            ArrayList<NeighborData> neighborsNeighborsDataList = pnm.getNeighborDataOfPGeoId(newState.getCode(),
                    newState.getElectionYear(), neighbor.getGeoId());
            ArrayList<Precinct> neighborsNeighbors = extractPrecinctsByNeighborDataList(
                    neighborsNeighborsDataList, newState.getPrecincts());

            difAffFound = false; // set if different affiliation is found around the neighbor
            for(Precinct p : neighborsNeighbors) {
                if(!p.getDistrict().equals(neighbor.getDistrict())) {
                    difAffFound = true;
                    break;
                }
            }
            neighbor.setBorder(difAffFound);
        }

        return true;
    }

    public ArrayList<AlgorithmAction> mainLogic(int iterations, AlgorithmInstance ai) {
        State algoState = ai.getAlgorithmState();
        if(algoState == null)
            return null;

        ArrayList<AlgorithmAction> algoActionList = new ArrayList<>();
        State oldState = sm.cloneState(algoState);
        int loopSteps = 0;
        while(loopSteps < iterations) {
            Precinct targetPrecinctOfOld = getRandomPrecinct(oldState.getBorderPrecincts());

            while (ai.getIgnored_precints().contains(targetPrecinctOfOld.getGeoId())){
                targetPrecinctOfOld = getRandomPrecinct(oldState.getBorderPrecincts());
            }
            State newState = generateNewDistrictBoundaries(targetPrecinctOfOld, oldState, ai);

            /* DELETE THIS AFTER IMPLEMENTING CONNECTED COMPONENTS CONSTRAINT ALGO */
            if(newState == null) {
                System.out.println("generateNewDistrictBoundaries() returned null");
                loopSteps++;
                continue;
            }

            if(!cm.getPopulationThres(newState, ai.getPopulationThreshold())) {
                loopSteps++;
                continue;
            }
            District oldAffDistrictOfNew = newState.getDistrictByDistrictCode(targetPrecinctOfOld.getDistrict().getCode());
            HashMap<String,  PrecinctNeighborRelation> precinctNeighborRelationMap
                    = pnm.getPrecinctNeighborRelationMap(newState.getCode(), newState.getElectionYear());
            if(!cm.isAllPrecinctsConnected(oldAffDistrictOfNew, precinctNeighborRelationMap)) {
                loopSteps++;
                continue;
            }

            double oldScore = cm.objectiveFunction(oldState, ai.getCompactnessWeight(), ai.getEfficiencyWeight());
            double newScore = cm.objectiveFunction(newState, ai.getCompactnessWeight(), ai.getEfficiencyWeight());
            if(newScore < oldScore){
                loopSteps++;
                continue;
            }
            // here, new state is qualified to be a move action

            Precinct targetPrecinctOfNew = newState.getPrecinctByPgeoid(targetPrecinctOfOld.getGeoId());
            // new affiliation of targetPrecinctOfNew has 0 as district_id
            District newAffDistrictOfNew = targetPrecinctOfNew.getDistrict();
            State originState = ai.getStateOrigin();
            Precinct originTargetPrecinct = originState.getPrecinctByPgeoid(targetPrecinctOfOld.getGeoId());
            District originOldAff = originState.getDistrictByDistrictCode(oldAffDistrictOfNew.getCode());
            District originNewAff = originState.getDistrictByDistrictCode(newAffDistrictOfNew.getCode());
            AlgorithmAction algoAct = new AlgorithmAction(originTargetPrecinct.getId(),
                    originOldAff.getId(),
                    originNewAff.getId(),
                    cm.getCompactnessMeasure(newAffDistrictOfNew),
                    cm.getCompactnessMeasure(oldAffDistrictOfNew),
                    cm.getEfficiencyGap(newState));
            // AlgorithmAction(int precinctId, int oldDistrictId, int newDistrictId, double newDistrictCompactness, double oldDistrictCompactness, double stateWideEfficiencyGap)
            algoActionList.add(algoAct);
            oldState = newState;
            loopSteps++;
        }
        // the procedure has been through the designated steps.
        ai.setAlgorithmState(oldState);

        return algoActionList;
    }
}
