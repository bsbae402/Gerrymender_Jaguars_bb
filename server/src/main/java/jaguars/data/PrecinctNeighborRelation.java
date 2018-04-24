package jaguars.data;

import java.util.ArrayList;

public class PrecinctNeighborRelation {
    private String fromGeoId;
    private ArrayList<NeighborData> neighborDataList;

    public PrecinctNeighborRelation(String fromGeoId) {
        this.fromGeoId = fromGeoId;
        neighborDataList = new ArrayList<>();
    }

    public void addNeighborData(NeighborData neighborData) {
        neighborDataList.add(neighborData);
    }

    public String getFromGeoId() {
        return fromGeoId;
    }

    public void setFromGeoId(String fromGeoId) {
        this.fromGeoId = fromGeoId;
    }

    public ArrayList<NeighborData> getNeighborDataList() {
        return neighborDataList;
    }

    public void setNeighborDataList(ArrayList<NeighborData> neighborDataList) {
        this.neighborDataList = neighborDataList;
    }
}
