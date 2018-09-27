package dbtest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;
import java.util.Set;
import java.lang.reflect.Type;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class DBTest01 {
    @Autowired
    private StudentRepository sr;

    @Test
    public void studentDBTestA() {
        Student stdA = new Student();
        stdA.setName("ppp");
        stdA.setStdId(333);

        sr.save(stdA);
    }

}
