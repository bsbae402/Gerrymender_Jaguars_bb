package jaguars.map.district;

import jaguars.map.precinct.Precinct;
import jaguars.map.state.State;
import jaguars.map.state.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictManager {
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private StateRepository stateRepository;

    public List<District> getAllDistricts() {
        List<District> districts = new ArrayList<>();
        for(District d : districtRepository.findAll())
            districts.add(d);
        return districts;
    }

    public List<District> getDistricts(int stateId) {
        State state = stateRepository.findOne(stateId);
        if(state == null)
            return null;
        return districtRepository.findByState(state);
    }

    public District getDistrictById(int districtId) {
        return districtRepository.findOne(districtId);
    }

    public List<District> getAdjacentDistrictsOfPrecinct(Precinct target) {
        return null;
    }
}
