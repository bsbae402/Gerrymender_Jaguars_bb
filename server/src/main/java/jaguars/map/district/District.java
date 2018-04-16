package jaguars.map.district;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jaguars.data.vd_district.VotingDataDistrict;
import jaguars.map.precinct.Precinct;
import jaguars.map.state.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class District {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Expose
    private String name;
    @Expose
    private int population;
    @Expose
    private int electionYear;
    @Expose
    private double area;
    @Expose
    private double perimeter;
    @Expose
    private String geoId;
    @Expose
    private int totalVotes;
    @Expose
    private String code;
    @Expose
    private boolean original;

    @JsonIgnore
    @Expose(serialize = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private State state;

    @Expose(serialize = false)
    @OneToMany(mappedBy = "district")
    private Set<Precinct> precincts = new HashSet<>();

    @Expose(serialize = false)
    @OneToMany(mappedBy = "district")
    private Set<VotingDataDistrict> votingDataDistricts = new HashSet<>();

    public District() {}

    public District(District district) {
        this.name = district.getName();
        this.population = district.getPopulation();
        this.electionYear = district.getElectionYear();
        this.area = district.getArea();
        this.perimeter = district.getPerimeter();
        this.geoId = district.getGeoId();
        this.totalVotes = district.getTotalVotes();
        this.code = district.getCode();
        this.original = false;
        for (VotingDataDistrict vdd : district.getVotingDataDistricts()) {
            VotingDataDistrict newVdd = new VotingDataDistrict(vdd);
            newVdd.setDistrict(this);
            this.getVotingDataDistricts().add(newVdd);
        }
    }

    public District(String name, int population, int electionYear, double area, double perimeter, String geoId, int totalVotes, String code, boolean original, State state) {
        this.name = name;
        this.population = population;
        this.electionYear = electionYear;
        this.area = area;
        this.perimeter = perimeter;
        this.geoId = geoId;
        this.totalVotes = totalVotes;
        this.code = code;
        this.original = original;
        this.state = state;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }

    public Set<VotingDataDistrict> getVotingDataDistricts() {
        return votingDataDistricts;
    }

    public void setVotingDataDistricts(Set<VotingDataDistrict> votingDataDistricts) {
        this.votingDataDistricts = votingDataDistricts;
    }

    @Override
    public String toString() {
        return "District{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", electionYear=" + electionYear +
                ", area=" + area +
                ", perimeter=" + perimeter +
                ", geoId='" + geoId + '\'' +
                ", totalVotes=" + totalVotes +
                ", code='" + code + '\'' +
                ", original=" + original +
                '}';
    }
}
