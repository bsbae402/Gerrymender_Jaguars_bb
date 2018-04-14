package jaguars.algorithm;

import jaguars.map.district.District;
import jaguars.map.district.DistrictManager;
import jaguars.map.precinct.PrecinctManager;
import jaguars.map.state.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class Algorithm {
    @Autowired
    private StateManager sm;

    @Autowired
    private DistrictManager dm;

    @Autowired
    private PrecinctManager pm;

    @Autowired
    private HttpSession httpSession;

    public void mainLogic() {
        // alogirhtm starts here
    }
}
