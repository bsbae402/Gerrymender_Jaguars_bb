package jaguars.map.precinct;

import jaguars.map.district.District;

import javax.persistence.*;

@Entity
public class Precinct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private int population;
    private int electionYear;
    private double area;
    private double perimeter;
    private String geoId;
    private int totalVotes;
    private boolean isBorder;
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    public Precinct() {
    }

    public Precinct(String name, int population, int electionYear, double area, double perimeter, String geoId, int totalVotes, boolean isBorder, String code, District district) {
        this.name = name;
        this.population = population;
        this.electionYear = electionYear;
        this.area = area;
        this.perimeter = perimeter;
        this.geoId = geoId;
        this.totalVotes = totalVotes;
        this.isBorder = isBorder;
        this.code = code;
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getElectionYear() {
        return electionYear;
    }

    public void setElectionYear(int electionYear) {
        this.electionYear = electionYear;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public void setBorder(boolean border) {
        isBorder = border;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Precinct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", electionYear=" + electionYear +
                ", area=" + area +
                ", perimeter=" + perimeter +
                ", geoId='" + geoId + '\'' +
                ", totalVotes=" + totalVotes +
                ", isBorder=" + isBorder +
                ", code='" + code + '\'' +
                ", district=" + district +
                '}';
    }
}
