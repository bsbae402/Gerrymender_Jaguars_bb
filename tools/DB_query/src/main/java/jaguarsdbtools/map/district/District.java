package jaguarsdbtools.map.district;

import com.google.gson.annotations.Expose;
import jaguarsdbtools.data.vd_district.VotingDataDistrict;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.state.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class District {
    @Expose
    @Id
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
    @Expose
    private boolean movable;
    @Expose
    private int medianIncome;

    @Expose(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @Expose(serialize = false)
    @OneToMany(mappedBy = "district", fetch = FetchType.EAGER)
    private Set<Precinct> precincts = new HashSet<>();

    @Expose(serialize = false)
    @OneToMany(mappedBy = "district", fetch = FetchType.EAGER)
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
        this.movable = false;
        this.medianIncome = 0;
        for (VotingDataDistrict vdd : district.getVotingDataDistricts()) {
            VotingDataDistrict newVdd = new VotingDataDistrict(vdd);
            newVdd.setDistrict(this);
            this.votingDataDistricts.add(newVdd);
        }
    }

    public District(int id, String name, int population, int electionYear, double area, double perimeter, String geoId, int totalVotes, String code, boolean original, boolean movable, int medianIncome) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.electionYear = electionYear;
        this.area = area;
        this.perimeter = perimeter;
        this.geoId = geoId;
        this.totalVotes = totalVotes;
        this.code = code;
        this.original = original;
        this.movable = movable;
        this.medianIncome = medianIncome;
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

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setMedianIncome(int medianIncome) {
        this.medianIncome = medianIncome;
    }

    public boolean isMovable() {
        return movable;
    }

    public int getMedianIncome() {
        return medianIncome;
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
