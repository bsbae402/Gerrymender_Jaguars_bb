package dbquery;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
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
public class StateExportQuery {
    @Autowired
    StateManager sm;

    @Test
    public void stateExportQuery() {
        List<State> allStates = sm.getAllStates();
        System.out.println("db query done");
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        JsonElement jeStates = gson.toJsonTree(allStates);
        System.out.println("gson parsing done");
        try {
            Writer writer = new FileWriter(AppConstants.PATH_OUT_DB_STATE);
            gson.toJson(jeStates, writer);
            System.out.println("file writing done");
            writer.flush(); // don't forget to flush
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
