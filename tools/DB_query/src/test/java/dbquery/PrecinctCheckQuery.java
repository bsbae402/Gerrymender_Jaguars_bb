package dbquery;

import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctManager;
import jaguarsdbtools.map.state.State;
import jaguarsdbtools.map.state.StateManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class PrecinctCheckQuery {
    @Autowired
    PrecinctManager pm;
    @Autowired
    StateManager sm;

    @Test
    public void checkPrecinctCodeUniqueness() {
        int stateId = 3; // WI
        State state = sm.getState(stateId);
        HashMap<String, Integer> precinctCodeCounter = new HashMap<>();
        for(Precinct p : state.getPrecincts()) {
            if(!precinctCodeCounter.keySet().contains(p.getCode())) {
                precinctCodeCounter.put(p.getCode(), 1);
            }
            else {
                int currentCount =  precinctCodeCounter.get(p.getCode());
                System.out.println("pcode: " + p.getCode() + " is found once more!");
                currentCount++;
                System.out.println("Its count is now: " + currentCount);
                precinctCodeCounter.replace(p.getCode(), currentCount);
            }
        }
    }
}
