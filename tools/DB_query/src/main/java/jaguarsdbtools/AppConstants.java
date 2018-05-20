package jaguarsdbtools;

public class AppConstants {
    public static final int[] CENSUS_YEARS = {1990, 2000, 2010};
    public static final String[] STATE_CODES = {"OH", "NH", "WI"};

    public static final String PATH_RESOURCES = "src/main/resources";
    public static final String PATH_JSON_FILES = PATH_RESOURCES + "/json_files";

    public static final String PATH_NEIGHBOR_FILES = PATH_RESOURCES + "/neighbor_files";
    public static final String PATH_PRECINCT_NEIGHBOR_RELATIONS_NH2010 = PATH_NEIGHBOR_FILES + "/NH_2010_precincts_neighbor_relations_v2.json";
    public static final String PATH_PRECINCT_NEIGHBOR_RELATIONS_OH2010 = PATH_NEIGHBOR_FILES + "/OH_2010_precincts_neighbor_relations_v3.json";
    public static final String PATH_PRECINCT_NEIGHBOR_RELATIONS_WI2010 = PATH_NEIGHBOR_FILES + "/WI_2010_precincts_neighbor_relations_v3.json";

    public static final String PATH_OUTFILES = PATH_RESOURCES + "/outfiles";
    public static final String PATH_OUT_DB_STATE = PATH_OUTFILES + "/db_state.json";
    public static final String PATH_OUT_DB_DISTRICT = PATH_OUTFILES + "/db_district.json";
    public static final String PATH_OUT_BD_PRECINCT = PATH_OUTFILES + "/db_precinct.json";


    public static final String PATH_OUT_DB_VDS = PATH_OUTFILES + "/db_vds.json";
    public static final String PATH_OUT_DB_VDD = PATH_OUTFILES + "/db_vdd.json";
    public static final String PATH_OUT_DB_VDP = PATH_OUTFILES + "/db_vdp.json";
}
