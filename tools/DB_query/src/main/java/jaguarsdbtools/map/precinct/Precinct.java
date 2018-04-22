package jaguarsdbtools.map.precinct;

import com.google.gson.annotations.Expose;
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinct;
import jaguarsdbtools.map.district.District;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Precinct {
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
    private boolean border;
    @Expose
    private String code;
    @Expose
    private boolean original;

    @Expose(serialize = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    @Expose(serialize = false)
    @OneToMany(mappedBy = "precinct")
    private Set<VotingDataPrecinct> votingDataPrecincts = new HashSet<>();

    public Precinct() {}

    public Precinct(Precinct precinct) {
        this.name = precinct.getName();
        this.population = precinct.getPopulation();
        this.electionYear = precinct.getElectionYear();
        this.area = precinct.getArea();
        this.perimeter = precinct.getPerimeter();
        this.geoId = precinct.getGeoId();
        this.totalVotes = precinct.getTotalVotes();
        this.border = precinct.isBorder();
        this.code = precinct.getCode();
        this.original = false;
        for (VotingDataPrecinct vdp : precinct.getVotingDataPrecincts()) {
            VotingDataPrecinct newVdp = new VotingDataPrecinct(vdp);
            newVdp.setPrecinct(this);
            this.votingDataPrecincts.add(newVdp);
        }
    }

    public Precinct(String name, int population, int electionYear, double area, double perimeter, String geoId, int totalVotes, boolean border, String code, boolean original, District district) {
        this.name = name;
        this.population = population;
        this.electionYear = electionYear;
        this.area = area;
        this.perimeter = perimeter;
        this.geoId = geoId;
        this.totalVotes = totalVotes;
        this.border = border;
        this.code = code;
        this.original = original;
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
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Set<VotingDataPrecinct> getVotingDataPrecincts() {
        return votingDataPrecincts;
    }

    public void setVotingDataPrecincts(Set<VotingDataPrecinct> votingDataPrecincts) {
        this.votingDataPrecincts = votingDataPrecincts;
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
                ", border=" + border +
                ", code='" + code + '\'' +
                ", original=" + original +
                '}';
    }
}
