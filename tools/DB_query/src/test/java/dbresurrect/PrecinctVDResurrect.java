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
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinct;
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinctManager;
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinctRepository;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class PrecinctVDResurrect {
    @Autowired
    private VotingDataPrecinctManager vdpm;

    @Autowired
    private VotingDataPrecinctRepository vdprepo;

    @Autowired
    private PrecinctManager precman;

    @Test
    public void votingDataPrecinctResurrect() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try {
            FileReader jsonfr = new FileReader(AppConstants.PATH_VDP_IMAGE_JSON);

            JsonValue dbVdpJV = Json.parse(jsonfr);
            JsonArray vdpJArr = dbVdpJV.asArray();

            for(JsonValue vdpJV : vdpJArr) {
                JsonObject vdpJO = vdpJV.asObject();

                int id = vdpJO.get("id").asInt();
                String politicalParty = vdpJO.get("political_party").asString();
                int votes = vdpJO.get("votes").asInt();
                boolean original = vdpJO.get("original").asBoolean();
                int precinctId = vdpJO.get("precinct_id").asInt();

                if(id > 7343) {
                    Precinct precint = precman.getPrecinct(precinctId);

                    VotingDataPrecinct vdp = new VotingDataPrecinct();
                    vdp.setId(id);
                    vdp.setPoliticalParty(PoliticalParty.valueOf(politicalParty));
                    vdp.setVotes(votes);
                    vdp.setOriginal(original);
                    vdp.setPrecinct(precint);

                    vdprepo.save(vdp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
