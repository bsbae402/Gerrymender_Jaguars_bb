package dbresurrect;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.map.state.State;
import com.google.gson.reflect.TypeToken;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.state.StateManager;
import jaguarsdbtools.map.state.StateRepository;
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
public class StateResurrect {
    private static final Type STATE_TYPE = new TypeToken<List<State>>(){}.getType();

    @Autowired
    private StateManager stman;

    @Autowired
    private StateRepository strep;

    @Test
    public void testResurrect() {
        Gson gson = new Gson();
        try{
            JsonReader reader = new JsonReader(new FileReader(AppConstants.PATH_STATE_IMAGE_JSON));
            ArrayList<State> stateList = gson.fromJson(reader, STATE_TYPE);
            for(State state : stateList) {
                System.out.println(state.getName());
            }
            for(State state : stateList) {
                strep.save(state);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
