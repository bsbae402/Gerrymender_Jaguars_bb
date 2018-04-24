package dbquery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctManager;
import jaguarsdbtools.util.CensusCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class PrecinctUpdateQuery {
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
    private PrecinctManager pm;

    @Test
    public void AddPrecinctPerimeters() {
        int censusYear = 2010;
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precinct_NH_2010.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeListGAP = new TypeToken<List<GeoidAreaPerimeter>>(){}.getType();
            List<GeoidAreaPerimeter> gapList = gson.fromJson(fileReader, typeListGAP);

            for(GeoidAreaPerimeter gap : gapList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(gap.geoid);
                // find only one precinct that is from "censusYear"
                Precinct censusPrecinct = null;
                for(Precinct p : precinctsOfSameGeoId) {
                    if(CensusCalculator.getCensusYear(p.getElectionYear()) == censusYear)
                        censusPrecinct = p;
                }
                // now, update the precinct perimeter
                censusPrecinct.setPerimeter(gap.perimeter);
                Precinct result = pm.updatePrecinct(censusPrecinct);
                System.out.println("pid " + result.getId() + " is updated");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void fixPrecinctPerimeters() {
        int censusYear = 2010;
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precinct_NH_2010.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeListGAP = new TypeToken<List<GeoidAreaPerimeter>>(){}.getType();
            List<GeoidAreaPerimeter> gapList = gson.fromJson(fileReader, typeListGAP);

            for(GeoidAreaPerimeter gap : gapList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(gap.geoid);
                // find only one precinct that is on "censusYear"
                Precinct censusPrecinct = null;
                for(Precinct p : precinctsOfSameGeoId) {
                    if(CensusCalculator.getCensusYear(p.getElectionYear()) == censusYear)
                        censusPrecinct = p;
                }
                // now, update the precinct perimeter
                censusPrecinct.setPerimeter(gap.perimeter);
                Precinct result = pm.updatePrecinct(censusPrecinct);
                System.out.println("pid " + result.getId() + " is updated");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
