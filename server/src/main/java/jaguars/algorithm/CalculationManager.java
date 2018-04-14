package jaguars.algorithm;

import jaguars.map.district.District;
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


}
