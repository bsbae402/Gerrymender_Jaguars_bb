package dbquery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
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
    private DistrictManager dm;
    @Autowired
    private PrecinctManager pm;

    @Test
    public void updatePrecinctPerimeters() {
        //String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precinct_NH_2010.json";
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precincts_WI_2010.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeListGAP = new TypeToken<List<GeoidAreaPerimeter>>(){}.getType();
            List<GeoidAreaPerimeter> gapList = gson.fromJson(fileReader, typeListGAP);

            for(GeoidAreaPerimeter gap : gapList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(gap.geoid);
                if(precinctsOfSameGeoId.size() > 1) {
                    System.out.println("There are two or more geoid precinct for geoid: " + gap.geoid);
                }
                if(precinctsOfSameGeoId == null || precinctsOfSameGeoId.size() == 0) {
                    System.out.println(gap.geoid + " doesn't exist in DB!");
                    continue;
                }
                Precinct firstOne = precinctsOfSameGeoId.get(0);
                firstOne.setPerimeter(gap.perimeter);
                //firstOne.setArea(gap.area);
                Precinct result = pm.updatePrecinct(firstOne);
                //System.out.println("pid " + result.getId() + " is updated");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updatePrecinctPerimetersAreas() {
        //String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precincts_WI_2010.json";
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precincts_OH_2010.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeListGAP = new TypeToken<List<GeoidAreaPerimeter>>(){}.getType();
            List<GeoidAreaPerimeter> gapList = gson.fromJson(fileReader, typeListGAP);

            for(GeoidAreaPerimeter gap : gapList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(gap.geoid);
                if(precinctsOfSameGeoId.size() > 1) {
                    System.out.println("There are two or more geoid precinct for geoid: " + gap.geoid);
                }
                if(precinctsOfSameGeoId == null || precinctsOfSameGeoId.size() == 0) {
                    System.out.println(gap.geoid + " doesn't exist in DB!");
                    continue;
                }
                Precinct firstOne = precinctsOfSameGeoId.get(0);
                if(firstOne.getArea() == 0 || firstOne.getPerimeter() == 0) {
                    firstOne.setPerimeter(gap.perimeter);
                    firstOne.setArea(gap.area);
                    Precinct result = pm.updatePrecinct(firstOne);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Assumption: precinct codes are currently VTDST10.
    // This will change them to districtCode + VTDST10
    @Test
    public void addPrecinctCodePrefix() {
        int stateId = 2; // going to update all the state's precincts codes
        List<District> districts = dm.getDistricts(stateId);
        for(District d : districts) {
            List<Precinct> precincts = pm.getPrecinctsByDistrictId(d.getId());
            for(Precinct p : precincts) {
                String newPCode = d.getCode() + p.getCode();
                p.setCode(newPCode);
                pm.updatePrecinct(p);
            }
        }
    }
}
