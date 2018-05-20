package dbquery;

import com.google.gson.*;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinct;
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinctManager;
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
public class VDPExportQuery {
    @Autowired
    private VotingDataPrecinctManager vdpm;

    @Test
    public void vdpExportQuery() {
        List<VotingDataPrecinct> allVDP = vdpm.getAllVotingDataPreincts();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        JsonArray jaVDPs = new JsonArray();
        for(VotingDataPrecinct vdp : allVDP) {
            System.out.println("current vdp id: " + vdp.getId());
            JsonElement jeVDP = gson.toJsonTree(vdp);
            JsonObject joVDP = jeVDP.getAsJsonObject();
            joVDP.addProperty("precinct_id", vdp.getPrecinct().getId());
            jaVDPs.add(joVDP);
        }

        try {
            System.out.println("file writing start");
            Writer writer = new FileWriter(AppConstants.PATH_OUT_DB_VDP);
            gson.toJson(jaVDPs, writer);
            System.out.println("file writing done");
            writer.flush(); // don't forget to flush
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
