package dbquery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jaguarsdbtools.AppConstants;
import jaguarsdbtools.JaguarsDBQuery;
import jaguarsdbtools.data.PoliticalParty;
import jaguarsdbtools.data.vd_district.VotingDataDistrict;
import jaguarsdbtools.data.vd_district.VotingDataDistrictManager;
import jaguarsdbtools.data.vd_precinct.VotingDataPrecinct;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.district.DistrictManager;
import jaguarsdbtools.map.precinct.Precinct;
import jaguarsdbtools.map.precinct.PrecinctManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JaguarsDBQuery.class})
public class PrecinctUpdateQuery {
    private class GeoidAreaPerimeter {
        public String geoid;
        public double area;
        public double perimeter;

        public GeoidAreaPerimeter(String geoid, double area, double perimeter) {
            this.geoid = geoid;
            this.area = area;
            this.perimeter = perimeter;
        }
    }
    @Autowired
    private DistrictManager dm;
    @Autowired
    private PrecinctManager pm;
    @Autowired
    private VotingDataDistrictManager vddm;

    @Test
    public void updatePrecinctPerimeters() {
        //String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precinct_NH_2010.json";
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precincts_WI_2010.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeListGAP = new TypeToken<List<GeoidAreaPerimeter>>(){}.getType();
            List<GeoidAreaPerimeter> gapList = gson.fromJson(fileReader, typeListGAP);

            for(GeoidAreaPerimeter gap : gapList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(gap.geoid);
                if(precinctsOfSameGeoId.size() > 1) {
                    System.out.println("There are two or more geoid precinct for geoid: " + gap.geoid);
                }
                if(precinctsOfSameGeoId == null || precinctsOfSameGeoId.size() == 0) {
                    System.out.println(gap.geoid + " doesn't exist in DB!");
                    continue;
                }
                Precinct firstOne = precinctsOfSameGeoId.get(0);
                firstOne.setPerimeter(gap.perimeter);
                //firstOne.setArea(gap.area);
                Precinct result = pm.updatePrecinct(firstOne);
                //System.out.println("pid " + result.getId() + " is updated");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updatePrecinctPerimetersAreas() {
        //String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precincts_WI_2010.json";
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/area_perimeter_precincts_OH_2010.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeListGAP = new TypeToken<List<GeoidAreaPerimeter>>(){}.getType();
            List<GeoidAreaPerimeter> gapList = gson.fromJson(fileReader, typeListGAP);

            for(GeoidAreaPerimeter gap : gapList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(gap.geoid);
                if(precinctsOfSameGeoId.size() > 1) {
                    System.out.println("There are two or more geoid precinct for geoid: " + gap.geoid);
                }
                if(precinctsOfSameGeoId == null || precinctsOfSameGeoId.size() == 0) {
                    System.out.println(gap.geoid + " doesn't exist in DB!");
                    continue;
                }
                Precinct firstOne = precinctsOfSameGeoId.get(0);
                if(firstOne.getArea() == 0 || firstOne.getPerimeter() == 0) {
                    firstOne.setPerimeter(gap.perimeter);
                    firstOne.setArea(gap.area);
                    Precinct result = pm.updatePrecinct(firstOne);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Assumption: precinct codes are currently VTDST10.
    // This will change them to districtCode + VTDST10
    @Test
    public void addPrecinctCodePrefix() {
        int stateId = 3; // going to update all the state's precincts codes
        List<District> districts = dm.getDistricts(stateId);
        for(District d : districts) {
            List<Precinct> precincts = pm.getPrecinctsByDistrictId(d.getId());
            for(Precinct p : precincts) {
                String newPCode = d.getCode() + p.getCode();
                String firstFourChar = p.getCode().substring(0, 4);
                if(firstFourChar.equals(newPCode))
                    continue;
                p.setCode(newPCode);
                pm.updatePrecinct(p);
            }
        }
    }

    private class PgeoidBorder {
        public String pgeoid;
        public boolean border;

        public PgeoidBorder(String pgeoid, boolean border) {
            this.pgeoid = pgeoid;
            this.border = border;
        }
    }
    @Test
    public void addPrecinctBorderness() {
        // int stateId = 3; // going to update all the state's precincts borderness: WI
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/isborder_WI_2010.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeList = new TypeToken<List<PgeoidBorder>>(){}.getType();
            List<PgeoidBorder> jobjList = gson.fromJson(fileReader, typeList);

            for(PgeoidBorder pgeoidBorder : jobjList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(pgeoidBorder.pgeoid);
                if(precinctsOfSameGeoId.size() > 1) {
                    System.out.println("There are two or more geoid precinct for geoid: " + pgeoidBorder.pgeoid);
                }
                if(precinctsOfSameGeoId == null || precinctsOfSameGeoId.size() == 0) {
                    System.out.println(pgeoidBorder.pgeoid + " doesn't exist in DB!");
                    continue;
                }
                Precinct firstOne = precinctsOfSameGeoId.get(0);
                firstOne.setBorder(pgeoidBorder.border);
                Precinct result = pm.updatePrecinct(firstOne);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updatePrecinctBorderness() {
        // int stateId = 3; // going to update all the state's precincts borderness: WI
//        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/isborder_WI_2010_v2.json";
        String jsonFilePath = AppConstants.PATH_JSON_FILES + "/isborder_OH_2010_v2.json";
        Gson gson = new GsonBuilder().create();
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            Type typeList = new TypeToken<List<PgeoidBorder>>(){}.getType();
            List<PgeoidBorder> jobjList = gson.fromJson(fileReader, typeList);

            for(PgeoidBorder pgeoidBorder : jobjList) {
                List<Precinct> precinctsOfSameGeoId = pm.getPrecinctsByGeoId(pgeoidBorder.pgeoid);
                if(precinctsOfSameGeoId.size() > 1) {
                    System.out.println("There are two or more geoid precinct for geoid: " + pgeoidBorder.pgeoid);
                }
                if(precinctsOfSameGeoId == null || precinctsOfSameGeoId.size() == 0) {
                    System.out.println(pgeoidBorder.pgeoid + " doesn't exist in DB!");
                    continue;
                }
                Precinct firstOne = precinctsOfSameGeoId.get(0);
                if(firstOne.isBorder() != pgeoidBorder.border) {
                    System.out.println(firstOne.getGeoId() + " borderness change to " + pgeoidBorder.border);
                    firstOne.setBorder(pgeoidBorder.border);
                    Precinct result = pm.updatePrecinct(firstOne);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void changePrecinctAffiliation() {
        // WI, Janesville Ward 33, 55105378250033: from WI02 to WI01
        List<Precinct> targetPrecincts = pm.getPrecinctsByGeoId("55105378250033");

        Precinct firstOnePrec = targetPrecincts.get(0);

        // WI District 1: 5501
        List<District> newAffiliations = dm.getDistrictsByGeoId("5501");
        District firstOneDist = newAffiliations.get(0);

        firstOnePrec.setDistrict(firstOneDist);
        String newCode = firstOneDist.getCode() + firstOnePrec.getCode().substring(4);
        firstOnePrec.setCode(newCode);
        pm.updatePrecinct(firstOnePrec);
    }

    // this will update precinct and the related two districts
    @Test
    public void renewPrecinct() {
        // 39049049AZA, id: 3630, FRANKLIN-E
        // COLS 01-C (39049049AAP, id: 2527)
        Precinct precinct = pm.getPrecinct(2527);

        Set<VotingDataPrecinct> vdpSet = precinct.getVotingDataPrecincts();

        // old aff: OH15, id: 17
        // old aff: OH03, id: 5
        District oldAff = dm.getDistrictById(5);
        Set<VotingDataDistrict> oldAffVDD = oldAff.getVotingDataDistricts();
        for(VotingDataDistrict vdd : oldAffVDD) {
            for(VotingDataPrecinct vdp : vdpSet) {
                if(vdd.getPoliticalParty() == vdp.getPoliticalParty()) {
                    // this is for old affiliation
                    int updatedDistVote = vdd.getVotes() - vdp.getVotes();
                    vdd.setVotes(updatedDistVote);
                    vddm.saveVotingDataDistrict(vdd);
                }
            }
        }

        // new aff: OH03, id: 5
        // new aff: OH15, id: 17
        District newAff = dm.getDistrictById(17);
        Set<VotingDataDistrict> newAffVDD = newAff.getVotingDataDistricts();
        for(VotingDataDistrict vdd : newAffVDD) {
            for(VotingDataPrecinct vdp : vdpSet) {
                if(vdd.getPoliticalParty() == vdp.getPoliticalParty()) {
                    // this is for new affiliation
                    int updatedDistVote = vdd.getVotes() + vdp.getVotes();
                    vdd.setVotes(updatedDistVote);
                    vddm.saveVotingDataDistrict(vdd);
                }
            }
        }

        // not border anymore ...
        //precinct.setBorder(false);

        // change affiliation
        precinct.setDistrict(newAff);
        String newCode = newAff.getCode() + precinct.getCode().substring(4);
        precinct.setCode(newCode);
        pm.updatePrecinct(precinct);
    }
}
