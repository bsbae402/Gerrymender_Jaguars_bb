package jaguars.data.global_storage;

import jaguars.map.state.State;

public class AlgorithmInstance {
    private State stateOrigin;  // will be same throughout the algorithm
    private State algorithmState;   // will continuously change for each update
    private double compactnessWeight;
    private double efficiencyWeight;
    private double populationThreshold;

    public AlgorithmInstance(State stateOrigin, State algorithmState,
                             double compactnessWeight, double efficiencyWeight, double populationThreshold) {
        this.stateOrigin = stateOrigin;
        this.algorithmState = algorithmState;
        this.compactnessWeight = compactnessWeight;
        this.efficiencyWeight = efficiencyWeight;
        this.populationThreshold = populationThreshold;
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

    public double getCompactnessWeight() {
        return compactnessWeight;
    }

    public void setCompactnessWeight(double compactnessWeight) {
        this.compactnessWeight = compactnessWeight;
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

    @Override
    public String toString() {
        return "AlgorithmInstance{" +
                "stateOrigin=" + stateOrigin +
                ", algorithmState=" + algorithmState +
                ", compactnessWeight=" + compactnessWeight +
                ", efficiencyWeight=" + efficiencyWeight +
                ", populationThreshold=" + populationThreshold +
                '}';
    }
}
