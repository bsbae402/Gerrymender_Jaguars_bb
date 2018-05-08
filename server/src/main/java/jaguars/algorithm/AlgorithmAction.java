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
    private double newDistrictCompactnessSch;
    @Expose
    private double oldDistrictCompactnessSch;
    @Expose
    private double newDistrictCompactnessPP;
    @Expose
    private double oldDistrictCompactnessPP;
    @Expose
    private double stateWideEfficiencyGap;

    public AlgorithmAction() {
    }

    public AlgorithmAction(int precinctId, int oldDistrictId, int newDistrictId,
                           double newDistrictCompactnessPP, double oldDistrictCompactnessPP,
                           double newDistrictCompactnessSch, double oldDistrictCompactnessSch,
                           double stateWideEfficiencyGap) {
        this.precinctId = precinctId;
        this.oldDistrictId = oldDistrictId;
        this.newDistrictId = newDistrictId;
        this.newDistrictCompactnessPP = newDistrictCompactnessPP;
        this.oldDistrictCompactnessPP = oldDistrictCompactnessPP;
        this.newDistrictCompactnessSch = newDistrictCompactnessSch;
        this.oldDistrictCompactnessSch = oldDistrictCompactnessSch;
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

    public double getnewDistrictCompactnessPP() {
        return newDistrictCompactnessPP;
    }

    public void setnewDistrictCompactnessPP(double newDistrictCompactnessPP) {
        this.newDistrictCompactnessPP = newDistrictCompactnessPP;
    }

    public double getoldDistrictCompactnessPP() {
        return oldDistrictCompactnessPP;
    }

    public void setoldDistrictCompactnessPP(double oldDistrictCompactnessPP) {
        this.oldDistrictCompactnessPP = oldDistrictCompactnessPP;
    }

    public double getNewDistrictCompactnessSch() {
        return newDistrictCompactnessSch;
    }

    public void setNewDistrictCompactnessSch(double newDistrictCompactnessSch) {
        this.newDistrictCompactnessSch = newDistrictCompactnessSch;
    }

    public double getOldDistrictCompactnessSch() {
        return oldDistrictCompactnessSch;
    }

    public void setOldDistrictCompactnessSch(double oldDistrictCompactnessSch) {
        this.oldDistrictCompactnessSch = oldDistrictCompactnessSch;
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
                ", newDistrictCompactnessPP=" + newDistrictCompactnessPP +
                ", oldDistrictCompactnessSch=" + oldDistrictCompactnessSch +
                ", stateWideEfficiencyGap=" + stateWideEfficiencyGap +
                '}';
    }
}
