package jaguars.data;

import jaguars.map.District;
import jaguars.map.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDataDistrictManager {
    @Autowired
    private VotingDataDistrictRepository vddr;
    @Autowired
    private DistrictRepository districtRepository;

    public List<VotingDataDistrict> getAllVotingDataDistricts() {
        List<VotingDataDistrict> votingDataDistrictList = new ArrayList<>();
        for(VotingDataDistrict vdd : vddr.findAll()) {
            System.out.println(vdd);
            votingDataDistrictList.add(vdd);
        }
        return votingDataDistrictList;
    }

    public List<VotingDataDistrict> getVotingDataDistrictListByDistrictId(int districtId) {
        District district = districtRepository.findOne(districtId);
        if(district == null)
            return null;
        return vddr.findByDistrict(district);
    }

}
