package jaguars.data.vd_state;

import jaguars.map.state.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VotingDataStateRepository  extends CrudRepository<VotingDataState, Integer> {
    List<VotingDataState> findByState(State state);
}
