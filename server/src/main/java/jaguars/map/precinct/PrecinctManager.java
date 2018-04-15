package jaguars.map.precinct;

import jaguars.map.district.District;
import jaguars.map.district.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrecinctManager {
    @Autowired
    private PrecinctRepository pr;
    @Autowired
    private DistrictRepository dr;

    public List<Precinct> getAllPrecincts() {
        List<Precinct> precincts = new ArrayList<>();
        for(Precinct p : pr.findAll())
            precincts.add(p);
        return precincts;
    }

    public List<Precinct> getPrecinctsByDistrictId(int districtId) {
        District district = dr.findOne(districtId);
        if(district == null)
            return null;
        return pr.findByDistrict(district);
    }
}
