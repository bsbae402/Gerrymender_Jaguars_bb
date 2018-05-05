package dbquery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;
import java.util.Set;
import java.lang.reflect.Type;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class DistrictUpdateQuery {
    private class GeoidAreaPerimeter {
        public String geoid;
        public double area;
        public double perimeter;

        public GeoidAreaPerimeter(String geoid, double area, double perimeter) {
            this.geoid = geoid;
            this.area = area;
            this.perimeter = perimeter;
        }
    }

    @Autowired
    private StateManager sm;
    @Autowired
    private DistrictManager dm;

    @Test
    public void updateDistrictsTotalVoteOfState() {
        int stateId = 1;
        State state = sm.getState(stateId);
        Set<District> districts = state.getDistricts();
        for(District d : districts) {
            Set<Precinct> precincts = d.getPrecincts();
            int totalVotes = 0;
            for(Precinct p : precincts)
                totalVotes += p.getTotalVotes();
            d.setTotalVotes(totalVotes);
            District whatsSaved = dm.updateDistrict(d);
            System.out.println(whatsSaved);
        }
    }

    @Test
    public void updateDistrictsAreaPerimeters() {
        int stateId = 1; // OH
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_district_OH_2011.json";

        Gson gson = new GsonBuilder().create();
        State state = sm.getState(stateId);

        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeListGAP = new TypeToken<List<GeoidAreaPerimeter>>(){}.getType();
            List<GeoidAreaPerimeter> gapList = gson.fromJson(fileReader, typeListGAP);

            for(GeoidAreaPerimeter gap : gapList) {
                District districtsOfGeoid = state.getDistrictByDgeoid(gap.geoid);
                districtsOfGeoid.setArea(gap.area);
                districtsOfGeoid.setPerimeter(gap.perimeter);
                dm.updateDistrict(districtsOfGeoid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
