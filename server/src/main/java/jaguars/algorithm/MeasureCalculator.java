package jaguars.algorithm;

import jaguars.AppConstants;
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
    public static ArrayList<Set<Precinct>> getConnectedComponentsInDistrict(
            District district,
            HashMap<String, PrecinctNeighborRelation> precinctNeighborRelationMap) {

//        Set<Precinct> precincts = district.getPrecincts();
//
//        ArrayList<Set<String>> connectedComponentList = new ArrayList<>();
//        HashMap<String, Set<String>> referenceToItsSet = new HashMap<>();
//
//        for(Precinct p : precincts) {
//            Set<String> connectedComponent = new HashSet<>();
//            connectedComponent.add(p.getGeoId());
//            connectedComponentList.add(connectedComponent);
//            referenceToItsSet.put(p.getGeoId(), connectedComponent);
//        }
//
//        PriorityQueue<String> pqGeoids = new PriorityQueue<>();
//        boolean expandedLastTime = true;
//        while(expandedLastTime) {
//            Set<String> firstCC = connectedComponentList.get(0);
//            ArrayList<String> elements = new ArrayList<>();
//            for(String geoid : firstCC) {
//                elements.add(geoid);
//            }
//
//
//        }

//
//        ArrayList<Set<Precinct>> connectedComponentSetList = new ArrayList<>();
//        HashMap<Precinct, Set<Precinct>> referenceToItsSet = new HashMap<>();
//
//        // individual precinct becomes its own set
//        for(Precinct p : precincts) {
//            Set<Precinct> connectedComponentSet = new HashSet<>();
//            connectedComponentSet.add(p);
//            connectedComponentSetList.add(connectedComponentSet);
//            referenceToItsSet.put(p, connectedComponentSet);
//        }
//
//        for(Precinct p : precincts) {
//            String pgeoid = p.getGeoId();
//            PrecinctNeighborRelation pnr = precinctNeighborRelationMap.get(pgeoid);
//            ArrayList<NeighborData> ndList =  pnr.getNeighborDataList();
//            for(NeighborData nd : ndList) {
//                nd.getToGeoId()
//            }
//
//            NeighborData firstNd = ndList.get(0);
//            firstNd.getToGeoId();
//
//            referenceToItsSet.get(p);
//        }
//
//        for(Set<Precinct> ccSet : connectedComponentSetList) {
//
//        }
//
//        for(Precinct p : precincts) {
//            String pgeoid = p.getGeoId();
//            PrecinctNeighborRelation pnr = precinctNeighborRelationMap.get(pgeoid);
//            ArrayList<NeighborData> ndList =  pnr.getNeighborDataList();
//
//
//            NeighborData firstNd = ndList.get(0);
//            firstNd.getToGeoId();
//        }
//        // ( precinctGeoId : PrecinctNeighborRelation }
//        PrecinctNeighborRelation pnr = precinctNeighborRelationMap.get("GEOIDASDFASDF");
//
//        boolean neighboringFound = true;
//        while(neighboringFound) {
//            neighboringFound = false;
//
//        }
//
//        // stubbed
        Set<Precinct> oneset = new HashSet<>();
        ArrayList<Set<Precinct>> listSizeOne = new ArrayList<>();
        listSizeOne.add(oneset);
        return listSizeOne;
    }
}
