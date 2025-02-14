package jaguarsdbtools.data.vd_district;

import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDataDistrictManager {
    @Autowired
    private VotingDataDistrictRepository vddr;
    @Autowired
    private DistrictRepository dr;

    public List<VotingDataDistrict> getAllVotingDataDistricts() {
        List<VotingDataDistrict> votingDataDistrictList = new ArrayList<>();
        for(VotingDataDistrict vdd : vddr.findAll()) {
            System.out.println(vdd);
            votingDataDistrictList.add(vdd);
        }
        return votingDataDistrictList;
    }

    public List<VotingDataDistrict> getVotingDataDistrictsByDistrictId(int districtId) {
        District district = dr.findOne(districtId);
        if(district == null)
            return null;
        return vddr.findByDistrict(district);
    }

    public VotingDataDistrict saveVotingDataDistrict(VotingDataDistrict vdd) {
        return vddr.save(vdd);
    }
}
