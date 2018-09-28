package dbresurrect;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctManager;
import jaguarsdbtools.map.precinct.PrecinctRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class PrecinctResurrect {
    @Autowired
    PrecinctManager precman;

    @Autowired
    PrecinctRepository precrepo;

    @Autowired
    DistrictManager distman;

    @Test
    public void precinctResurrect() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try{
            FileReader jsonfr = new FileReader(AppConstants.PATH_PRECINCT_IMAGE_JSON);
            JsonValue dbPrecJV = Json.parse(jsonfr);
            JsonArray dbPrecJArr = dbPrecJV.asArray();

            for(JsonValue precJV : dbPrecJArr.values()) {
                JsonObject precJO = precJV.asObject();

                int id = precJO.get("id").asInt();
                String name = precJO.get("name").asString();
                int population = precJO.get("population").asInt();
                int electionYear = precJO.get("election_year").asInt();
                double area = precJO.get("area").asDouble();
                double perimeter = precJO.get("perimeter").asDouble();
                String geoId = precJO.get("geo_id").asString();
                int totalVotes = precJO.get("total_votes").asInt();
                boolean border = precJO.get("border").asBoolean();
                String code = precJO.get("code").asString();
                boolean original = precJO.get("original").asBoolean();
                boolean movable = precJO.get("movable").asBoolean();
                int districtId = precJO.get("district_id").asInt();

                District district = distman.getDistrictById(districtId);

                Precinct precinct = new Precinct();
                precinct.setId(id);
                precinct.setName(name);
                precinct.setPopulation(population);
                precinct.setElectionYear(electionYear);
                precinct.setArea(area);
                precinct.setPerimeter(perimeter);
                precinct.setGeoId(geoId);
                precinct.setTotalVotes(totalVotes);
                precinct.setBorder(border);
                precinct.setCode(code);
                precinct.setOriginal(original);
                precinct.setMovable(movable);
                precinct.setDistrict(district);

                precrepo.save(precinct);
            }
        } catch (Exception e) {

        }
    }
}
