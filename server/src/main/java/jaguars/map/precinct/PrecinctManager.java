package jaguars.map.precinct;

import jaguars.map.state.State;
import jaguars.map.state.StateRepository;
import jaguars.map.district.District;
import jaguars.map.district.DistrictRepository;
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

    @Autowired
    private StateRepository stateRepository;

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
        District district = districtRepository.findOne(districtId);
        if(district == null)
            return null;
        List<Precinct> precinctList = precinctRepository.findByDistrictAndIsBorder(district, true);
        return precinctList;
    }

    public List<Precinct> getBorderPrecinctsOfState(int stateId) {
        State state = stateRepository.findOne(stateId);
        if(state == null)
            return null;
        List<District> districtList = districtRepository.findByState(state);
        List<Precinct> borderPrecinctList = new ArrayList<>();
        for(District d : districtList) {
            List<Precinct> borderOfDistrict = getBorderPrecinctsOfDistrict(d.getId());
            borderPrecinctList.addAll(borderOfDistrict);
        }
        return borderPrecinctList;
    }
}
