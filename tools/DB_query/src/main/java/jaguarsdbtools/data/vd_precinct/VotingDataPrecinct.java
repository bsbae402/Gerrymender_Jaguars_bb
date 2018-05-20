package jaguarsdbtools.data.vd_precinct;

import com.google.gson.annotations.Expose;
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.map.precinct.Precinct;

import javax.persistence.*;

@Entity
public class VotingDataPrecinct {
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "precinct_id")
    private Precinct precinct;

    public VotingDataPrecinct() {}

    public VotingDataPrecinct(VotingDataPrecinct vdp) {
        this.politicalParty = vdp.getPoliticalParty();
        this.votes = vdp.getVotes();
        this.original = false;
    }

    public VotingDataPrecinct(PoliticalParty politicalParty, int votes, boolean original, Precinct precinct) {
        this.politicalParty = politicalParty;
        this.votes = votes;
        this.original = original;
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

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }

    @Override
    public String toString() {
        return "VotingDataPrecinct{" +
                "id=" + id +
                ", politicalParty=" + politicalParty +
                ", votes=" + votes +
                ", original=" + original +
                '}';
    }
}
