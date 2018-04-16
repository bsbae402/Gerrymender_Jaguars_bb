package jaguars.map.state;

import jaguars.map.district.District;
import jaguars.map.precinct.Precinct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class StateManager {
    @Autowired
    private StateRepository sr;

    @Autowired
    private HttpSession httpSession;

    public StateManager() {}

    public ArrayList<State> getAllStates() {
        ArrayList<State> allStates = new ArrayList<>();
        for(State s : sr.findAll()) {
            allStates.add(s);
        }
        return allStates;
    }

    public State getState(int stateId) {
        return sr.findOne(stateId);
    }

    public List<State> getStatesByNameYear(String name, int electionYear) {
        return sr.findByNameAndElectionYear(name, electionYear);
    }

    public List<State> getOriginalStates() {
        return sr.findByOriginal(true);
    }

    public void setSessionState(State state) {
        httpSession.setAttribute("state", (Object)state);
    }

    public State getSessionsState() {
        return (State)httpSession.getAttribute("state");
    }

    public List<String> getAllStateCodes() {
        List<String> stateCodes = new ArrayList<>();
        for(State s : sr.findAll()) {
            if(!stateCodes.contains(s.getCode()))
                stateCodes.add(s.getCode());
        }
        return stateCodes;
    }

    public State cloneState(State state) {
        State newState = new State(state);
        Set<District> newDistricts = new HashSet<>();

        for (District d : state.getDistricts()) {
            District newDistrict = new District(d);
            newDistrict.setState(newState);
            newDistricts.add(newDistrict);

            for (Precinct p : d.getPrecincts()) {
                Precinct newPrecinct = new Precinct(p);
                newPrecinct.setDistrict(newDistrict);
                newDistrict.getPrecincts().add(newPrecinct);
            }
        }
        return newState;
    }
}
