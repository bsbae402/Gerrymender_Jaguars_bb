package dbresurrect;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.district.DistrictManager;
import jaguarsdbtools.map.district.DistrictRepository;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
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
public class DistrictResurrect {
    //private static final Type DISTRICT_TYPE = new TypeToken<List<District>>(){}.getType();

    @Autowired
    private DistrictManager distman;

    @Autowired
    private DistrictRepository distrepo;

    @Autowired
    private StateManager stman;

    @Test
    public void districtResurrect() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try{
            FileReader jsonfr = new FileReader(AppConstants.PATH_DISTRICT_IMAGE_JSON);

            JsonValue dbDistJV = Json.parse(jsonfr);
            JsonArray distJArr = dbDistJV.asArray();

            for(JsonValue distJV : distJArr.values()) {
                JsonObject distJObj = distJV.asObject();
                int id = distJObj.get("id").asInt();
                String name = distJObj.get("name").asString();
                int population = distJObj.get("population").asInt();
                int electionYear = distJObj.get("election_year").asInt();
                double area = distJObj.get("area").asDouble();
                double perimeter = distJObj.get("perimeter").asDouble();
                String geoId = distJObj.get("geo_id").asString();
                int totalVotes = distJObj.get("total_votes").asInt();
                String code = distJObj.get("code").asString();
                boolean original = distJObj.get("original").asBoolean();
                boolean movable = distJObj.get("movable").asBoolean();
                int medianIncome = distJObj.get("median_income").asInt();
                int stateId = distJObj.get("state_id").asInt();

                State state = stman.getState(stateId);

                District district = new District();
                district.setId(id);
                district.setName(name);
                district.setPopulation(population);
                district.setElectionYear(electionYear);
                district.setArea(area);
                district.setPerimeter(perimeter);
                district.setGeoId(geoId);
                district.setTotalVotes(totalVotes);
                district.setCode(code);
                district.setOriginal(original);
                district.setMovable(movable);
                district.setMedianIncome(medianIncome);
                district.setState(state);

                distrepo.save(district);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
