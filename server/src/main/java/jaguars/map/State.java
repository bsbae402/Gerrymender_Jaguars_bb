package jaguars.map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class State {
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

    public State() {
    }

    public State(int id, String name, int population, int electionYear, double area, double perimeter, String geoId, int totalVotes) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.electionYear = electionYear;
        this.area = area;
        this.perimeter = perimeter;
        this.geoId = geoId;
        this.totalVotes = totalVotes;
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

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", electionYear=" + electionYear +
                ", area=" + area +
                ", perimeter=" + perimeter +
                ", geoId='" + geoId + '\'' +
                ", totalVotes=" + totalVotes +
                '}';
    }
}
