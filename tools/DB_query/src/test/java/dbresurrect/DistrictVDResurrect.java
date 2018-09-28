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
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.data.vd_district.VotingDataDistrict;
import jaguarsdbtools.data.vd_district.VotingDataDistrictManager;
import jaguarsdbtools.data.vd_district.VotingDataDistrictRepository;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class DistrictVDResurrect {
    @Autowired
    private VotingDataDistrictManager vddm;

    @Autowired
    private VotingDataDistrictRepository vddrepo;

    @Autowired
    private DistrictManager distman;

    @Test
    public void votingDataDistrictResurrect() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try{
            FileReader jsonfr = new FileReader(AppConstants.PATH_VDD_IMDAGE_JSON);

            JsonValue dbVDDjv = Json.parse(jsonfr);
            JsonArray vddJArr = dbVDDjv.asArray();

            for(JsonValue vddJV : vddJArr) {
                JsonObject vddJO = vddJV.asObject();
                int id = vddJO.get("id").asInt();
                String politicalParty = vddJO.get("political_party").asString();
                int votes = vddJO.get("votes").asInt();
                boolean original = vddJO.get("original").asBoolean();
                int districtId = vddJO.get("district_id").asInt();
                District dist = distman.getDistrictById(districtId);

                VotingDataDistrict vdd = new VotingDataDistrict();
                vdd.setId(id);
                vdd.setPoliticalParty(PoliticalParty.valueOf(politicalParty));
                vdd.setVotes(votes);
                vdd.setOriginal(original);
                vdd.setDistrict(dist);

                vddrepo.save(vdd);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
