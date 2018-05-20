package dbquery;

import com.google.gson.*;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.vd_district.VotingDataDistrict;
import jaguarsdbtools.data.vd_district.VotingDataDistrictManager;
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
public class VDDExportQuery {
    @Autowired
    private VotingDataDistrictManager vddm;

    @Test
    public void vddExportQuery() {
        List<VotingDataDistrict> allVDD = vddm.getAllVotingDataDistricts();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        JsonArray jaVDDs = new JsonArray();
        for(VotingDataDistrict vdd : allVDD) {
            System.out.println("currend vdd id: " + vdd.getId());
            JsonElement jeVDD = gson.toJsonTree(vdd);
            JsonObject joVDD = jeVDD.getAsJsonObject();
            joVDD.addProperty("district_id", vdd.getDistrict().getId());
            jaVDDs.add(joVDD);
        }

        try {
            System.out.println("file writing start");
            Writer writer = new FileWriter(AppConstants.PATH_OUT_DB_VDD);
            gson.toJson(jaVDDs, writer);
            System.out.println("file writing done");
            writer.flush(); // don't forget to flush
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
