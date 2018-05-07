package jaguarsdbtools.data.vd_district;

import com.google.gson.annotations.Expose;
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.map.district.District;

import javax.persistence.*;

@Entity
public class VotingDataDistrict {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Expose
    private PoliticalParty politicalParty;
    @Expose
    private int votes;
    @Expose
    private boolean original;

    @Expose(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    public VotingDataDistrict() {}

    public VotingDataDistrict(VotingDataDistrict vdd) {
        this.politicalParty = vdd.getPoliticalParty();
        this.votes = vdd.getVotes();
        this.original = false;
    }

    public VotingDataDistrict(PoliticalParty politicalParty, int votes, boolean original, District district) {
        this.politicalParty = politicalParty;
        this.votes = votes;
        this.original = original;
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
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

    @Override
    public String toString() {
        return "VotingDataDistrict{" +
                "id=" + id +
                ", politicalParty=" + politicalParty +
                ", votes=" + votes +
                ", original=" + original +
                '}';
    }
}
