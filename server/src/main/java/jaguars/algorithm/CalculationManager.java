package jaguars.algorithm;

import jaguars.AppConstants;
import jaguars.data.PoliticalParty;
import jaguars.data.vd_district.VotingDataDistrict;
import jaguars.map.district.District;
import jaguars.map.state.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CalculationManager {

    private double getCompactnessMeasure(District district){
        double area = district.getArea();
        double perimeter = district.getPerimeter();
        double[] measures = {MeasureCalculator.calculateCompactnessPP(area, perimeter),
                MeasureCalculator.calculateCompactnessSch(area, perimeter)};
        return (measures[0] + measures[1]) / 2;
    }

    private double getEfficiencyGap(State state) {
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
}
