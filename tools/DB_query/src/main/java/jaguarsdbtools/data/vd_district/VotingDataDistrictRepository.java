package jaguarsdbtools.data.vd_district;

import jaguarsdbtools.map.district.District;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VotingDataDistrictRepository extends CrudRepository<VotingDataDistrict, Integer> {
    List<VotingDataDistrict> findByDistrict(District district);
}
