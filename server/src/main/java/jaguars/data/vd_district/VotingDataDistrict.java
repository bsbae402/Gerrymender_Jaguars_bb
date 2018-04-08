package jaguars.data.vd_district;

import jaguars.data.PoliticalParty;
import jaguars.map.district.District;

import javax.persistence.*;

@Entity
public class VotingDataDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private PoliticalParty politicalParty;
    private int votes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    public VotingDataDistrict() {
    }

    public VotingDataDistrict(PoliticalParty politicalParty, int votes, District district) {
        this.politicalParty = politicalParty;
        this.votes = votes;
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
                ", district=" + district +
                '}';
    }
}
