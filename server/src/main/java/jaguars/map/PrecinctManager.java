package jaguars.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrecinctManager {
    @Autowired
    private PrecinctRepository precinctRepository;

    @Autowired
    private DistrictRepository districtRepository;

    public List<Precinct> getAllPrecincts() {
        List<Precinct> precincts = new ArrayList<>();
        for(Precinct p : precinctRepository.findAll())
            precincts.add(p);
        return precincts;
    }

    public List<Precinct> getPrecinctListByDistrictId(int districtId) {
        District district = districtRepository.findOne(districtId);
        if(district == null)
            return null;
        List<Precinct> precinctList = precinctRepository.findByDistrict(district);
        return precinctList;
    }

    public Precinct getPrecinctById(int precinctId) {
        return precinctRepository.findOne(precinctId);
    }

    public List<Precinct> getBorderPrecinctsOfDistrict(int districtId) {
        return null;
    }

    public List<Precinct> getBorderPrecinctsOfState(int stateId) {
        return null;
    }
}
