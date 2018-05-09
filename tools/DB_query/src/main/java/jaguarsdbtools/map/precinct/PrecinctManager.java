package jaguarsdbtools.map.precinct;

import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictRepository;
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

    public Precinct getPrecinct(int pid) {
        return pr.findOne(pid);
    }

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

    public List<Precinct> getPrecinctsByGeoId(String geoId) {
        return pr.findByGeoId(geoId);
    }

    public Precinct updatePrecinct(Precinct updatedPrecinct) {
        return pr.save(updatedPrecinct);
    }
}
