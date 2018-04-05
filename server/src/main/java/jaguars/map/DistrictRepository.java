package jaguars.map;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DistrictRepository extends CrudRepository<District, Integer> {
    List<District> findByState(State state);
}
