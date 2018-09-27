package dbresurrect;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.data.vd_state.VotingDataState;
import jaguarsdbtools.data.vd_state.VotingDataStateManager;
import jaguarsdbtools.data.vd_state.VotingDataStateRepository;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class StateVDResurrect {
    //private static final Type VDS_TYPE = new TypeToken<List<VotingDataState>>(){}.getType();
    @Autowired
    VotingDataStateManager vdsm;

    @Autowired
    VotingDataStateRepository vdsrepo;

    @Autowired
    StateManager stman;

    @Test
    public void votingDataStateResurrect() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try{
            FileReader jsonfr = new FileReader(AppConstants.PATH_VDS_IMAGE_JSON);

            JsonValue dbVdsJV = Json.parse(jsonfr);
            JsonArray vdsJArr = dbVdsJV.asArray();

            for(JsonValue vdsJV : vdsJArr.values()) {
                JsonObject vdsJObj = vdsJV.asObject();
                int id = vdsJObj.get("id").asInt();
                String polip = vdsJObj.get("political_party").asString();
                int votes = vdsJObj.get("votes").asInt();
                boolean original = vdsJObj.get("original").asBoolean();
                int stateId = vdsJObj.get("state_id").asInt();
                State state = stman.getState(stateId);

                VotingDataState vds = new VotingDataState();
                vds.setId(id);
                vds.setPoliticalParty(PoliticalParty.valueOf(polip));
                vds.setVotes(votes);
                vds.setOriginal(original);
                vds.setState(state);

                vdsm.saveVotingDataState(vds);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
