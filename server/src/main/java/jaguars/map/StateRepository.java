package jaguars.map;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {
    List<State> findByNameAndElectionYear(String name, int electionYear);
}
