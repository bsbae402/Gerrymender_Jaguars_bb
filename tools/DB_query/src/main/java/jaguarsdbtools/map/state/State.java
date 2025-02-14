package jaguarsdbtools.map.state;

import com.google.gson.annotations.Expose;
import jaguarsdbtools.data.vd_state.VotingDataState;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.precinct.Precinct;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
public class State {
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

    @Expose(serialize = false)
    @OneToMany(mappedBy = "state", fetch = FetchType.EAGER)
    private Set<District> districts = new HashSet<>();

    @Expose(serialize = false)
    @OneToMany(mappedBy = "state", fetch = FetchType.EAGER)
    private Set<VotingDataState> votingDataStates = new HashSet<>();

    public State() {}

    public State(State state){
        this.name = state.getName();
        this.population = state.getPopulation();
        this.electionYear = state.getElectionYear();
        this.area = state.getArea();
        this.perimeter = state.getPerimeter();
        this.geoId = state.getGeoId();
        this.totalVotes = state.getTotalVotes();
        this.code = state.getCode();
        this.original = false;
        for (VotingDataState vds : state.getVotingDataStates()) {
            VotingDataState newVds = new VotingDataState(vds);
            vds.setState(this);
            this.votingDataStates.add(newVds);
        }
    }

    public State(String name, int population, int electionYear, double area, double perimeter, String geoId, int totalVotes, String code, boolean original) {
        this.name = name;
        this.population = population;
        this.electionYear = electionYear;
        this.area = area;
        this.perimeter = perimeter;
        this.geoId = geoId;
        this.totalVotes = totalVotes;
        this.code = code;
        this.original = original;
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

    public Set<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    public Set<VotingDataState> getVotingDataStates() {
        return votingDataStates;
    }

    public void setVotingDataStates(Set<VotingDataState> votingDataStates) {
        this.votingDataStates = votingDataStates;
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
                ", code='" + code + '\'' +
                ", original=" + original +
                '}';
    }

    public ArrayList<Precinct> getBorderPrecincts() {
        ArrayList<Precinct> borderPrecincts = new ArrayList<>();
        for(District d : getDistricts()) {
            for(Precinct p : d.getPrecincts()) {
                if(p.isBorder())
                    borderPrecincts.add(p);
            }
        }
        return borderPrecincts;
    }

    public District getDistrictByDistrictCode(String districtCode) {
        for(District d : districts) {
            if(d.getCode().equals(districtCode))
                return d;
        }
        return null;
    }

    public Precinct getPrecinctByPrecinctCode(String precinctCode) {
        for(District d : districts) {
            for(Precinct p : d.getPrecincts()) {
                if(p.getCode().equals(precinctCode))
                    return p;
            }
        }
        return null;
    }

    public District getDistrictByDgeoid(String dgeoid) {
        for(District d : districts) {
            if(d.getGeoId().equals(dgeoid))
                return d;
        }
        return null;
    }

    public ArrayList<Precinct> getPrecincts() {
        ArrayList<Precinct> precincts = new ArrayList<>();
        for(District d : getDistricts()) {
            precincts.addAll(d.getPrecincts());
        }
        return precincts;
    }
}
