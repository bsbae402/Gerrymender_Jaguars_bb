package jaguars.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class StateManager {
    TreeMap<Integer, State> states;

    @Autowired
    private StateRepository sr;

    public StateManager() {
        states = new TreeMap<Integer, State>();
    }

    public ArrayList<State> getAllStates() {
        ArrayList<State> allStates = new ArrayList<>();
        for(State s : sr.findAll()) {
            System.out.println(s);
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
}
