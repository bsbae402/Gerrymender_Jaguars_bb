package manipulation;

public class Manipulation {
    public static void main(String[] args) {
        PrecinctNameGeoIdMapper pgm = new PrecinctNameGeoIdMapper();

        pgm.extractPrecinctNameAndGeoId(Constants.PATH_PRECINCT_GEOJSON_OH2010,
                Constants.PATH_OUTPUT_DIR + "/precinct_geoid_OH2010.json");
        pgm.extractPrecinctNameAndGeoId(Constants.PATH_PRECINCT_GEOJSON_NH2010,
                Constants.PATH_OUTPUT_DIR + "/precinct_geoid_NH2010.json");



//        DistrictNameGeoIdMapper dngm = new DistrictNameGeoIdMapper();
//        dngm.extractDistrictNameAndGeoId(Constants.PATH_DISTRICT_GEOJSON_NH2010,
//                Constants.PATH_OUTPUT_DIR + "/district_geoid_NH2010.json");
    }
}
