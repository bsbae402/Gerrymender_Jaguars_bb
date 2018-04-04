package jaguars.map;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrecinctRepository extends CrudRepository<Precinct, Integer> {
    List<Precinct> findByDistrict(District district);
}
