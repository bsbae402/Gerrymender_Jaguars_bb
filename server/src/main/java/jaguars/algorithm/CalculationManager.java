package jaguars.algorithm;

import jaguars.AppConstants;
import jaguars.data.PrecinctNeighborRelation;
import jaguars.data.vd_district.VotingDataDistrict;
import jaguars.data.vd_precinct.VotingDataPrecinct;
import jaguars.map.district.District;
import jaguars.map.precinct.Precinct;
import jaguars.map.state.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Service
public class CalculationManager {

    public District findLowestDistrictPop(State state) {
        ArrayList<District> districts = new ArrayList<>(state.getDistricts());
        int min = Integer.MAX_VALUE;
        District retDis = new District();

        for (District d : districts) {
            if (d.getPopulation() < min) {
                retDis = d;
                min = d.getPopulation();
            }
        }
        return retDis;
    }

    public District findHighestDistrictPop(State state) {
        ArrayList<District> districts = new ArrayList<>(state.getDistricts());
        int max = Integer.MIN_VALUE;
        District retDis = new District();

        for (District d : districts) {
            if (d.getPopulation() > max) {
                retDis = d;
                max = d.getPopulation();
            }
        }
        return retDis;
    }

    public Precinct findLowestPrecinctPop(District district) {
        ArrayList<Precinct> precincts = new ArrayList<>(district.getPrecincts());
        int min = Integer.MAX_VALUE;
        Precinct retPre = new Precinct();

        for (Precinct p : precincts) {
            if (p.getPopulation() < min) {
                retPre = p;
                min = p.getPopulation();
            }
        }
        return retPre;
    }

    public Precinct findHighestPrecinctPop(District district) {
        ArrayList<Precinct> precincts = new ArrayList<>(district.getPrecincts());
        int max = Integer.MIN_VALUE;
        Precinct retPre = new Precinct();

        for (Precinct p : precincts) {
            if (p.getPopulation() > max) {
                retPre = p;
                max = p.getPopulation();
            }
        }
        return retPre;
    }

    public double getStateAverageCompactness(State state) {
        ArrayList<District> districts = new ArrayList<>(state.getDistricts());
        double sum = 0;

        for (District d : districts) {
            sum += getCompactnessMeasure(d);
        }
        return sum / districts.size();
    }

    public double getCompactnessMeasure(District district){
        double area = district.getArea();
        double perimeter = district.getPerimeter();
        double[] measures = {MeasureCalculator.calculateCompactnessPP(area, perimeter),
                MeasureCalculator.calculateCompactnessSch(area, perimeter)};
        return (measures[0] + measures[1]) / 2;
    }

    public double getEfficiencyGap(State state) {
        ArrayList<District> districts = new ArrayList<>(state.getDistricts());
        int demWasted = 0;
        int repWasted = 0;

        for (District d : districts) {
            for (VotingDataDistrict vdd : d.getVotingDataDistricts()) {
                int wastedVotes = vdd.getVotes();

                if (vdd.getVotes() > (d.getTotalVotes()/2) + 1) {
                    wastedVotes -= ((d.getTotalVotes()/2) + 1);
                }
                switch (vdd.getPoliticalParty()){
                    case REP:
                        repWasted += wastedVotes;
                        break;
                    case DEM:
                        demWasted += wastedVotes;
                        break;
                    default:
                        repWasted += wastedVotes/2;
                        demWasted += wastedVotes/2;
                        break;
                }
            }
        }
        return Math.abs(MeasureCalculator.calculateEfficiencyGap(state.getTotalVotes(), repWasted, demWasted));
    }

    public double getEfficiencyGap(District district) {
        ArrayList<Precinct> precincts = new ArrayList<>(district.getPrecincts());
        int demWasted = 0;
        int repWasted = 0;

        for (Precinct p : precincts){
            for (VotingDataPrecinct vdp : p.getVotingDataPrecincts()){
                int wastedVotes = vdp.getVotes();

                if (vdp.getVotes() > (p.getTotalVotes()/2) + 1) {
                    wastedVotes -= ((p.getTotalVotes()/2) + 1);
                }
                switch (vdp.getPoliticalParty()){
                    case REP:
                        repWasted += wastedVotes;
                        break;
                    case DEM:
                        demWasted += wastedVotes;
                        break;
                    default:
                        repWasted += wastedVotes/2;
                        demWasted += wastedVotes/2;
                        break;
                }
            }
        }
        return Math.abs(MeasureCalculator.calculateEfficiencyGap(district.getTotalVotes(), repWasted, demWasted));
    }

    public double objectiveFunction(State state){
        ArrayList<District> districts = new ArrayList<>(state.getDistricts());
        double compactnessSum = 0;
        double avgCompactness;

        for (District d : state.getDistricts()) {
            compactnessSum += getCompactnessMeasure(d);
        }
        avgCompactness = compactnessSum / (double)districts.size();
        double efficiencyGapScore = 1 - getEfficiencyGap(state);

        return (AppConstants.DEFAULT_COMPACTNESS_WEIGHT * avgCompactness) + (AppConstants.DEFAULT_EFFICIENCY_WEIGHT * efficiencyGapScore);
    }

    public boolean getPopulationThres(State state) {
        int[] districtPops = new int[state.getDistricts().size()];
        int i = 0;

        for (District d : state.getDistricts()) {
            districtPops[i] = d.getPopulation();
            i++;
        }
        return MeasureCalculator.calculatePopThreshold(state.getPopulation(), districtPops);
    }

    public boolean isAllPrecinctsConnected(District district,
                                           HashMap<String, PrecinctNeighborRelation> precinctNeighborRelationMap) {
        ArrayList<Set<Precinct>> connectedComponentList
                = MeasureCalculator.getConnectedComponentsInDistrict(district, precinctNeighborRelationMap);
        if(connectedComponentList.size() > 1)
            return false;
        else
            return true;
    }
}
