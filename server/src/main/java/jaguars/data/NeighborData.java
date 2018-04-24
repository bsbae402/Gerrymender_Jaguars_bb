package jaguars.data;

public class NeighborData {
    private String toGeoId;
    private double contactLength;

    public NeighborData(String toGeoId, double contactLength) {
        this.toGeoId = toGeoId;
        this.contactLength = contactLength;
    }

    public String getToGeoId() {
        return toGeoId;
    }

    public void setToGeoId(String toGeoId) {
        this.toGeoId = toGeoId;
    }

    public double getContactLength() {
        return contactLength;
    }

    public void setContactLength(double contactLength) {
        this.contactLength = contactLength;
    }

    @Override
    public String toString() {
        return "NeighborData{" +
                "toGeoId='" + toGeoId + '\'' +
                ", contactLength=" + contactLength +
                '}';
    }
}
