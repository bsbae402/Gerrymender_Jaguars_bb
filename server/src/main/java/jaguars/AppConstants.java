package jaguars;

public class AppConstants {
    public static final int[] CENSUS_YEARS = {1990, 2000, 2010};
    public static final String[] STATE_CODES = {"OH", "NH", "WI"};

    // Algorithm
    public static final double DEFAULT_COMPACTNESS_WEIGHT = 0.5;
    public static final double DEFAULT_EFFICIENCY_WEIGHT = 0.5;
    public static final double DEFAULT_POPULATION_THRESHOLD = 0.01;
    public static final int MAX_LOOP_STEPS = 10000;

    // API JSON names
    public static final String JSON_NAME_VOTING_DATA = "voting_data";

    // Path GeoJSON
    public static final String PATH_RESOURCES = "src/main/resources";
    public static final String PATH_GEOJSONS = PATH_RESOURCES + "/geojsons";
    public static final String PATH_STATE_GEOJSON_OH2010 = PATH_GEOJSONS + "/state_boundaries/state_tl_2010_39_OH.json";
    public static final String PATH_STATE_GEOJSON_NH2010 = PATH_GEOJSONS + "/state_boundaries/state_tl_2010_NH.json";
    public static final String PATH_STATE_GEOJSON_WI2010 = PATH_GEOJSONS + "/state_boundaries/state_tl_2010_WI.json";
    public static final String PATH_DISTRICT_GEOJSON_OH2010 = PATH_GEOJSONS + "/district_boundaries/OH_districts_2011_REVISED.json";
    public static final String PATH_DISTRICT_GEOJSON_NH2010 = PATH_GEOJSONS + "/district_boundaries/district_2010_NH_jaguars.json";
    public static final String PATH_DISTRICT_GEOJSON_WI2010 = PATH_GEOJSONS + "/district_boundaries/WI_district_2011_jaguars.json";
    public static final String PATH_PRECINCT_GEOJSON_OH2010 = PATH_GEOJSONS + "/precinct_boundaries/OH_precincts_2010_fixed_v2.json";
    public static final String PATH_PRECINCT_GEOJSON_NH2010 = PATH_GEOJSONS + "/precinct_boundaries/NH_precincts_2010.json";
    public static final String PATH_PRECINCT_GEOJSON_WI2010 = PATH_GEOJSONS + "/precinct_boundaries/WI_wards_2010_jaguars_v2.json";

    // Path json
    public static final String PATH_JSONS = PATH_RESOURCES + "/jsons";
    public static final String PATH_PRECINCT_NEIGHBOR_RELATIONS_NH2010 = PATH_JSONS + "/NH_2010_precincts_neighbor_relations.json";
    public static final String PATH_PRECINCT_NEIGHBOR_RELATIONS_OH2010 = PATH_JSONS + "/OH_2010_precincts_neighbor_relations.json";
    public static final String PATH_PRECINCT_NEIGHBOR_RELATIONS_WI2010 = PATH_JSONS + "/WI_2010_precincts_neighbor_relations.json";

    // return codes
    public static final int ON_SUCCESS = 0;
    public static final int ON_ERROR = 1;
    public static final int USER_NOT_OK = -1;
}
