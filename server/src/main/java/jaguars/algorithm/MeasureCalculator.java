package jaguars.algorithm;

import jaguars.AppConstants;

import java.util.ArrayList;

public class MeasureCalculator {

    public static double calculateCompactnessPP(double area, double perimeter){
        return 4 * Math.PI * (area / (perimeter * perimeter));
    }

    public static double calculateCompactnessSch(double area, double perimeter){
        double radius = Math.sqrt(area / Math.PI);
        double circumference = 2 * Math.PI * radius;
        return 1 / (perimeter / circumference);
    }

    public boolean checkPopulationThreshold(int statePop, ArrayList<Integer> districtPops) {
        int delta = (int)(statePop * AppConstants.DEFAULT_POPULATION_THRESHOLD);
        int equalPop = statePop / districtPops.size();
        int lowerThreshold = equalPop - delta;
        int higherThreshold = equalPop + delta;
        for (Integer districtPop : districtPops){
            if (districtPop < lowerThreshold || districtPop > higherThreshold)
                return false;
        }
        return true;
    }


}
