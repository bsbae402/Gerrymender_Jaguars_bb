package jaguars;

public class AppConstants {
    public static final int[] CENSUS_YEARS = {1990, 2000, 2010};
    public static final String[] STATE_CODES = {"OH", "NH", "WI"};

    // Algorithm
    public static final double DEFAULT_COMPACT_WEIGHT_PP = 1.0;
    public static final double DEAFULT_COMPACT_WEIGHT_SCH = 1.0;
    public static final double DEFAULT_POPULATION_THRESHOLD = 0.01;
    public static final int MAX_LOOP_STEPS = 100;

    // API JSON names
    public static final String JSON_NAME_VOTING_DATA = "voting_data";

    // Path
    public static final String PATH_RESOURCES = "src/main/resources";
    public static final String PATH_GEOJSONS = PATH_RESOURCES + "/geojsons";
    public static final String PATH_STATE_GEOJSON_OH2010 = PATH_GEOJSONS + "/state_boundaries/state_tl_2010_39_OH.json";
    public static final String PATH_STATE_GEOJSON_NH2010 = PATH_GEOJSONS + "/state_boundaries/state_tl_2010_NH.json";
    public static final String PATH_STATE_GEOJSON_WI2010 = PATH_GEOJSONS + "/state_boundaries/state_tl_2010_WI.json";
    public static final String PATH_DISTRICT_GEOJSON_OH2010 = PATH_GEOJSONS + "/district_boundaries/district_tl_2010_OH.json";
    public static final String PATH_DISTRICT_GEOJSON_NH2010 = PATH_GEOJSONS + "/district_boundaries/district_tl_2010_NH.json";
    public static final String PATH_DISTRICT_GEOJSON_WI2010 = PATH_GEOJSONS + "/district_boundaries/district_tl_2010_WI.json";
    public static final String PATH_PRECINCT_GEOJSON_OH2010 = PATH_GEOJSONS + "/precinct_boundaries/OH_precincts_2010.json";
    public static final String PATH_PRECINCT_GEOJSON_NH2010 = PATH_GEOJSONS + "/precinct_boundaries/NH_precincts_2010.json";
    public static final String PATH_PRECINCT_GEOJSON_WI2010 = PATH_GEOJSONS + "/precinct_boundaries/WI_precincts_2010.json";
}
