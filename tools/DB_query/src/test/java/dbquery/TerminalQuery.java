package dbquery;

import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class TerminalQuery {
    @Autowired
    private PrecinctManager pm;

    @Test
    public void gettingCodes() {
        for(Precinct p : pm.getAllPrecincts()){
            System.out.println(p.getCode());
        }
    }

    @Test
    public void gettingIds() {
        for(Precinct p : pm.getAllPrecincts()){
            System.out.println(p.getId());
        }
    }
}
