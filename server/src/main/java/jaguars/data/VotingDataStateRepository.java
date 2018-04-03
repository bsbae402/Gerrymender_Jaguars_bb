package jaguars.data;

import jaguars.map.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VotingDataStateRepository  extends CrudRepository<VotingDataState, Integer> {
    List<VotingDataState> findByState(State state);
}
