package jaguars.algorithm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.annotations.Expose;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AlgorithmAction {
    @Expose
    private int precinctId;
    @JsonIgnore
    @Expose(serialize = false)
    private int oldDistrictId;
    @Expose
    private int newDistrictId;
    @Expose
    private double newDistrictCompactness;
    @Expose
    private double oldDistrictCompactness;
    @Expose
    private double stateWideEfficiencyGap;

    public AlgorithmAction() {
    }

    public AlgorithmAction(int precinctId, int oldDistrictId, int newDistrictId, double newDistrictCompactness, double oldDistrictCompactness, double stateWideEfficiencyGap) {
        this.precinctId = precinctId;
        this.oldDistrictId = oldDistrictId;
        this.newDistrictId = newDistrictId;
        this.newDistrictCompactness = newDistrictCompactness;
        this.oldDistrictCompactness = oldDistrictCompactness;
        this.stateWideEfficiencyGap = stateWideEfficiencyGap;
    }

    public int getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(int precinctId) {
        this.precinctId = precinctId;
    }

    public int getOldDistrictId() {
        return oldDistrictId;
    }

    public void setOldDistrictId(int oldDistrictId) {
        this.oldDistrictId = oldDistrictId;
    }

    public int getNewDistrictId() {
        return newDistrictId;
    }

    public void setNewDistrictId(int newDistrictId) {
        this.newDistrictId = newDistrictId;
    }

    public double getNewDistrictCompactness() {
        return newDistrictCompactness;
    }

    public void setNewDistrictCompactness(double newDistrictCompactness) {
        this.newDistrictCompactness = newDistrictCompactness;
    }

    public double getOldDistrictCompactness() {
        return oldDistrictCompactness;
    }

    public void setOldDistrictCompactness(double oldDistrictCompactness) {
        this.oldDistrictCompactness = oldDistrictCompactness;
    }

    public double getStateWideEfficiencyGap() {
        return stateWideEfficiencyGap;
    }

    public void setStateWideEfficiencyGap(double stateWideEfficiencyGap) {
        this.stateWideEfficiencyGap = stateWideEfficiencyGap;
    }

    @Override
    public String toString() {
        return "AlgorithmAction{" +
                "precinctId=" + precinctId +
                ", oldDistrictId=" + oldDistrictId +
                ", newDistrictId=" + newDistrictId +
                ", newDistrictCompactness=" + newDistrictCompactness +
                ", oldDistrictCompactness=" + oldDistrictCompactness +
                ", stateWideEfficiencyGap=" + stateWideEfficiencyGap +
                '}';
    }
}
