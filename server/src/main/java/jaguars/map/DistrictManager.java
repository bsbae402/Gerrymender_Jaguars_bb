package jaguars.map;

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

    public List<District> getDistrictListByStateId(int stateId) {
        State state = stateRepository.findOne(stateId);
        System.out.println("state found: " + state);
        if(state == null)
            return null;
        List<District> districtList = districtRepository.findByState(state);
        // it is possible that state exists, but there is no districts under the state
        return districtList;
    }

    public District getDistrictById(int districtId) {
        return districtRepository.findOne(districtId);
    }
}
