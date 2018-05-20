package dbquery;

import com.google.gson.*;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctManager;
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
public class PrecinctExportQuery {
    @Autowired
    PrecinctManager pm;

    @Test
    public void precinctExportQuery() {
        List<Precinct> allPrec = pm.getAllPrecincts();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        JsonArray jaPrecincts = new JsonArray();
        for(Precinct p : allPrec) {
            System.out.println("current Precinct: " + p.getId());
            JsonElement jePrecinct = gson.toJsonTree(p);
            JsonObject joPrecinct = jePrecinct.getAsJsonObject();
            joPrecinct.addProperty("district_id", p.getDistrict().getId());
            jaPrecincts.add(joPrecinct);
        }

        try {
            System.out.println("file writing start");
            Writer writer = new FileWriter(AppConstants.PATH_OUT_BD_PRECINCT);
            gson.toJson(jaPrecincts, writer);
            System.out.println("file writing done");
            writer.flush(); // don't forget to flush
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
