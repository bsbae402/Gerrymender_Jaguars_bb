package jaguarsdbtools.data.vd_state;

import com.google.gson.annotations.Expose;
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.map.state.State;

import javax.persistence.*;

@Entity
public class VotingDataState {
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
    @JoinColumn(name = "state_id")
    private State state;

    public VotingDataState() {}

    public VotingDataState(VotingDataState vds) {
        this.politicalParty = vds.getPoliticalParty();
        this.votes = vds.getVotes();
        this.original = false;
    }

    public VotingDataState(PoliticalParty politicalParty, int votes, boolean original, State state) {
        this.politicalParty = politicalParty;
        this.votes = votes;
        this.original = original;
        this.state = state;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "VotingDataState{" +
                "id=" + id +
                ", politicalParty=" + politicalParty +
                ", votes=" + votes +
                ", original=" + original +
                '}';
    }
}
