package dbquery;

import com.google.gson.*;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.vd_state.VotingDataState;
import jaguarsdbtools.data.vd_state.VotingDataStateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class VDSExportQuery {
    @Autowired
    private VotingDataStateManager vdsm;

    @Test
    public void vdsExportQuery() {
        List<VotingDataState> allVDSs = vdsm.getAllVotingDataStates();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        JsonArray jaVDS = new JsonArray();
        for(VotingDataState vds : allVDSs) {
            JsonElement jeVDS = gson.toJsonTree(vds);
            JsonObject joVDS = jeVDS.getAsJsonObject();
            joVDS.addProperty("state_id", vds.getState().getId());
            jaVDS.add(joVDS);
        }

        try {
            System.out.println("file writing start");
            Writer writer = new FileWriter(AppConstants.PATH_OUT_DB_VDS);
            gson.toJson(jaVDS, writer);
            System.out.println("file writing done");
            writer.flush(); // don't forget to flush
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
