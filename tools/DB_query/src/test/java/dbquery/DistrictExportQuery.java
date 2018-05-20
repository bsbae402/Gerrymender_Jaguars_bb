package dbquery;

import com.google.gson.*;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
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
public class DistrictExportQuery {
    @Autowired
    DistrictManager dm;

    @Test
    public void districtExportQuery() {
        List<District> allDist = dm.getAllDistricts();
        System.out.println("end of db query");
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        //JsonElement jeDistricts = gson.toJsonTree(allDist);
        //System.out.println(je.toString());
        JsonArray jaDistricts = new JsonArray();
        for(District d : allDist) {
            System.out.println("current district id: " + d.getId());
            JsonElement jeOneDist = gson.toJsonTree(d);
            JsonObject joOneDist = jeOneDist.getAsJsonObject();
            joOneDist.addProperty("state_id", d.getState().getId());
            jaDistricts.add(joOneDist);
        }

        try {
            System.out.println("file writing start");
            Writer writer = new FileWriter(AppConstants.PATH_OUT_DB_DISTRICT);
            gson.toJson(jaDistricts, writer);
            System.out.println("file writing done");
            writer.flush(); // don't forget to flush
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
