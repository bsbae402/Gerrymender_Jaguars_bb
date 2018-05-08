package jaguars.analytics;

import com.google.gson.*;
import jaguars.AppConstants;
import jaguars.algorithm.CalculationManager;
import jaguars.data.global_storage.AlgorithmInstance;
import jaguars.map.district.District;
import jaguars.map.district.DistrictManager;
import jaguars.map.precinct.Precinct;
import jaguars.map.precinct.PrecinctManager;
import jaguars.map.state.State;
import jaguars.map.state.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class AnalyticsController {
    @Autowired
    private StateManager sm;
    @Autowired
    private DistrictManager dm;
    @Autowired
    private PrecinctManager pm;
    @Autowired
    CalculationManager cm;

    @RequestMapping(value = "/analytics/state", method = RequestMethod.GET)
    public String getStateAnalytics(@RequestParam("state_id") int stateId) {
        State state = sm.getState(stateId);
        District lowest = cm.findLowestDistrictPop(state);
        District highest = cm.findHighestDistrictPop(state);

        JsonObject retObj = new JsonObject();
        retObj.addProperty("polsby_compactness_score", cm.getStateAverageCompactnessPP(state));
        retObj.addProperty("schwartzberg_compactness_score", cm.getStateAverageCompactnessSch(state));
        retObj.addProperty("efficiency_gap_score", cm.getEfficiencyGap(state));
        retObj.addProperty("lowest_district_population", lowest.getPopulation());
        retObj.addProperty("Lowest_pop_geoid ", lowest.getGeoId());
        retObj.addProperty("highest_district_population", highest.getPopulation());
        retObj.addProperty("highest_pop_geoid ", highest.getGeoId());
        retObj.addProperty("number_of_border_precincts ", state.getBorderPrecincts().size());

        return retObj.toString();
    }

    @RequestMapping(value = "/analytics/district", method = RequestMethod.GET)
    public String getDistrictAnalytics(@RequestParam("district_id") int districtId) {
        District district = dm.getDistrictById(districtId);
        Precinct lowest = cm.findLowestPrecinctPop(district);
        Precinct highest = cm.findHighestPrecinctPop(district);

        JsonObject retObj = new JsonObject();
        retObj.addProperty("polsby_compactness_score", cm.getCompactnessMeasurePP(district));
        retObj.addProperty("schwartzberg_compactness_score", cm.getCompactnessMeasureSch(district));
        retObj.addProperty("efficiency_gap_score", cm.getEfficiencyGap(district));
        retObj.addProperty("lowest_precinct_population", lowest.getPopulation());
        retObj.addProperty("Lowest_pop_geoid", lowest.getGeoId());
        retObj.addProperty("highest_precinct_population", highest.getPopulation());
        retObj.addProperty("highest_pop_geoid", highest.getGeoId());
        retObj.addProperty("number_of_precincts", district.getPrecincts().size());
        retObj.addProperty("median_income", district.getMedianIncome());
        retObj.addProperty("incumbent", district.getIncumbent());

        return retObj.toString();
    }
}
