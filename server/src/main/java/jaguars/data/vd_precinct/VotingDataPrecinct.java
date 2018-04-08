package jaguars.data.vd_precinct;

import jaguars.data.PoliticalParty;
import jaguars.map.precinct.Precinct;

import javax.persistence.*;

@Entity
public class VotingDataPrecinct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private PoliticalParty politicalParty;
    private int votes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "precinct_id")
    private Precinct precinct;

    public VotingDataPrecinct() {
    }

    public VotingDataPrecinct(PoliticalParty politicalParty, int votes, Precinct precinct) {
        this.politicalParty = politicalParty;
        this.votes = votes;
        this.precinct = precinct;
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

    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }

    @Override
    public String toString() {
        return "VotingDataState{" +
                "id=" + id +
                ", politicalParty=" + politicalParty +
                ", votes=" + votes +
                ", precinct=" + precinct +
                '}';
    }
}
