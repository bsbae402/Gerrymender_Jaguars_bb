package jaguars.map;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.TreeMap;

@Service
public class StateManager {
    TreeMap<Integer, State> states;

    public StateManager() {
        states = new TreeMap<Integer, State>();
        State nh1 = new State(0, "New Hampshire", 2016);
        State nh2 = new State(1, "New Hampshire", 2014);
        State oh1 = new State(100, "Ohio", 2016);
        State oh2 = new State(101, "Ohio", 2014);
        State wi1 = new State(201, "Wisconsin", 2016);
        states.put(nh1.getId(), nh1);
        states.put(nh2.getId(), nh2);
        states.put(oh1.getId(), oh1);
        states.put(oh2.getId(), oh2);
        states.put(wi1.getId(), wi1);
    }

    public ArrayList<State> getAllStates() {
        return new ArrayList<State>(states.values());
    }

    public State getState(int stateId) {
        return states.get(stateId);
    }
}
