package jaguars.algorithm;

import jaguars.data.NeighborData;
import jaguars.data.PrecinctNeighborRelation;
import jaguars.map.district.District;
import jaguars.map.precinct.Precinct;

import java.util.*;

public final class MeasureCalculator {

    public static double calculateCompactnessPP(double area, double perimeter){
        return 4 * Math.PI * (area / (perimeter * perimeter));
    }

    public static double calculateCompactnessSch(double area, double perimeter){
        double radius = Math.sqrt(area / Math.PI);
        double circumference = 2 * Math.PI * radius;
        return 1 / (perimeter / circumference);
    }

    public static boolean calculatePopThreshold(int statePop, int[] districtPops, double popThres) {
        int delta = (int)(statePop * popThres);
        int equalPop = statePop / districtPops.length;
        int lowerThreshold = equalPop - delta;
        int higherThreshold = equalPop + delta;
        for (Integer districtPop : districtPops){
            if (districtPop < lowerThreshold || districtPop > higherThreshold)
                return false;
        }
        return true;
    }

    public static double calculateEfficiencyGap(int total, int rep, int dem) {
        return (rep*1.0 - dem*1.0) / total*1.0;
    }

    // neighborDataList is state-level precinct neighbor relation data holder
    // RETURN: geoid set list. If some geoids are in same set, it means they are in same connected component
    public static ArrayList<Set<String>> getConnectedComponentsInDistrict(
            District district,
            HashMap<String, PrecinctNeighborRelation> precinctNeighborRelationMap) {
        // ( precinctGeoId : PrecinctNeighborRelation } pair in precinctNeighborRelationMap

        // first, build from -> to_list hash map
        HashMap<String, ArrayList<String>> fromToList = new HashMap<>();
        for(PrecinctNeighborRelation pnr : precinctNeighborRelationMap.values()) {
            ArrayList<NeighborData> ndList = pnr.getNeighborDataList();
            ArrayList<String> toGeoidList = new ArrayList<>();
            for(NeighborData nd : ndList)
                toGeoidList.add(nd.getToGeoId());
            fromToList.put(pnr.getFromGeoId(), toGeoidList);
        }

        Set<Precinct> precinctsOfDistrict = district.getPrecincts();

        ArrayList<String> pgeoidsOfDistrict = new ArrayList<>();
        ArrayList<Set<String>> connectedComponentList = new ArrayList<>();
        HashMap<String, Set<String>> referenceToItsSet = new HashMap<>();

        for(Precinct p : precinctsOfDistrict) {
            Set<String> connectedComponent = new HashSet<>();
            connectedComponent.add(p.getGeoId());
            connectedComponentList.add(connectedComponent);
            referenceToItsSet.put(p.getGeoId(), connectedComponent);
            pgeoidsOfDistrict.add(p.getGeoId());
        }

        for(Precinct p : precinctsOfDistrict) {
            ArrayList<String> toGeoidList = fromToList.get(p.getGeoId());
            Set<String> itsSet = referenceToItsSet.get(p.getGeoId());
            for(String neighborGeoid : toGeoidList) {
                // if the neighbor is in same district
                // if itsSet not yet contains the neighbor
                if(pgeoidsOfDistrict.contains(neighborGeoid)
                        && !itsSet.contains(neighborGeoid)) {
                    // neighbors set will be removed from the memory
                    Set<String> neighborsSet = referenceToItsSet.get(neighborGeoid);
                    for(String elementFromNeighborSet : neighborsSet) {
                        referenceToItsSet.put(elementFromNeighborSet, itsSet);
                        itsSet.add(elementFromNeighborSet);
                    }
                    connectedComponentList.remove(neighborsSet);
                }
            }
        }

        // stubbed
//        Set<Precinct> oneset = new HashSet<>();
//        ArrayList<Set<Precinct>> listSizeOne = new ArrayList<>();
//        listSizeOne.add(oneset);
        if(connectedComponentList.size() > 1) {
            System.out.println("District: " + district.getGeoId() + " has " + connectedComponentList.size() + " components");
        }
        return connectedComponentList;
    }
}
