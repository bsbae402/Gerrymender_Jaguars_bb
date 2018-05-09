package jaguarsdbtools.util;

import jaguarsdbtools.data.NeighborData;
import jaguarsdbtools.data.PrecinctNeighborRelation;
import jaguarsdbtools.map.district.District;
import jaguarsdbtools.map.precinct.Precinct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class MeasureCalculator {
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

        if(connectedComponentList.size() > 1) {
            System.out.println("District: " + district.getGeoId() + " has " + connectedComponentList.size() + " components");
        }
        return connectedComponentList;
    }
}
