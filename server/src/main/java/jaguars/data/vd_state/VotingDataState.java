package jaguars.data.vd_state;

import jaguars.data.PoliticalParty;
import jaguars.map.state.State;

import javax.persistence.*;

@Entity
public class VotingDataState {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private PoliticalParty politicalParty;
    private int votes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private State state;

    public VotingDataState() {
    }

    public VotingDataState(PoliticalParty politicalParty, int votes, State state) {
        this.politicalParty = politicalParty;
        this.votes = votes;
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
                ", state=" + state +
                '}';
    }
}
