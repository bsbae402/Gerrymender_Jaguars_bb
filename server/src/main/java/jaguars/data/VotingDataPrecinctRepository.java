package jaguars.data;

import jaguars.map.Precinct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VotingDataPrecinctRepository extends CrudRepository<VotingDataPrecinct, Integer> {
    List<VotingDataPrecinct> findByPrecinct(Precinct precinct);
}
