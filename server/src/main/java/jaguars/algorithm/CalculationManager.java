package jaguars.algorithm;

import jaguars.data.PoliticalParty;
import jaguars.data.vd_district.VotingDataDistrict;
import jaguars.map.district.District;
import jaguars.map.state.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CalculationManager {

    public double[] getCompactnessMeasures(District targetDistrict){
        double area = targetDistrict.getArea();
        double perimeter = targetDistrict.getPerimeter();
        double[] measures = {MeasureCalculator.calculateCompactnessPP(area, perimeter),
                MeasureCalculator.calculateCompactnessSch(area, perimeter)};
        return measures;
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
        return MeasureCalculator.calculateEfficiencyGap(state.getTotalVotes(), repWasted, demWasted);
    }
}
