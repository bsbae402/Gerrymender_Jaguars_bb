package jaguars.data.global_storage;

import jaguars.map.precinct.Precinct;
import jaguars.map.state.State;

import java.util.HashSet;

public class AlgorithmInstance {
    private State stateOrigin;  // will be same throughout the algorithm
    private State algorithmState;   // will continuously change for each update
    private double compactnessWeightPP;
    private double compactnessWeightSch;
    private double efficiencyWeight;
    private double populationThreshold;
    private HashSet<String> ignored_precints; // geoid
    private HashSet<String> ignored_districts; // geoid
    private HashSet<String> usedPrecincts;
    private int heuristic;

    public AlgorithmInstance(State stateOrigin, State algorithmState,
                             double compactnessWeightPP, double compactnessWeightSch, double efficiencyWeight, double populationThreshold,
                             HashSet<String> ignored_precints, HashSet<String> ignored_districts, int heuristic) {
        this.stateOrigin = stateOrigin;
        this.algorithmState = algorithmState;
        this.compactnessWeightPP = compactnessWeightPP;
        this.compactnessWeightSch = compactnessWeightSch;
        this.efficiencyWeight = efficiencyWeight;
        this.populationThreshold = populationThreshold;
        this.ignored_precints = ignored_precints;
        this.ignored_districts = ignored_districts;
        this.heuristic = heuristic;
        usedPrecincts = new HashSet<>();
    }

    public State getStateOrigin() {
        return stateOrigin;
    }

    public void setStateOrigin(State stateOrigin) {
        this.stateOrigin = stateOrigin;
    }

    public State getAlgorithmState() {
        return algorithmState;
    }

    public void setAlgorithmState(State algorithmState) {
        this.algorithmState = algorithmState;
    }

    public double getCompactnessWeightPP() {
        return compactnessWeightPP;
    }

    public void setCompactnessWeightPP(double compactnessWeightPP) {
        this.compactnessWeightPP = compactnessWeightPP;
    }

    public double getCompactnessWeightSch() {
        return compactnessWeightSch;
    }

    public void setCompactnessWeight(double compactnessWeightSch) {
        this.compactnessWeightSch = compactnessWeightSch;
    }

    public double getEfficiencyWeight() {
        return efficiencyWeight;
    }

    public void setEfficiencyWeight(double efficiencyWeight) {
        this.efficiencyWeight = efficiencyWeight;
    }

    public double getPopulationThreshold() {
        return populationThreshold;
    }

    public void setPopulationThreshold(double populationThreshold) {
        this.populationThreshold = populationThreshold;
    }

    public HashSet<String> getIgnored_precints() {
        return ignored_precints;
    }

    public void setIgnored_precints(HashSet<String> ignored_precints) {
        this.ignored_precints = ignored_precints;
    }

    public HashSet<String> getIgnored_districts() {
        return ignored_districts;
    }

    public void setIgnored_districts(HashSet<String> ignored_districts) {
        this.ignored_districts = ignored_districts;
    }

    public HashSet<String> getUsedPrecincts() {
        return usedPrecincts;
    }

    public void setUsedPrecincts(HashSet<String> usedPrecincts) {
        this.usedPrecincts = usedPrecincts;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }


    @Override
    public String toString() {
        return "AlgorithmInstance{" +
                "stateOrigin=" + stateOrigin +
                ", algorithmState=" + algorithmState +
                ", compactnessWeightPP=" + compactnessWeightPP +
                ", compactnessWeightSch=" + compactnessWeightSch +
                ", efficiencyWeight=" + efficiencyWeight +
                ", populationThreshold=" + populationThreshold +
                '}';
    }
}
