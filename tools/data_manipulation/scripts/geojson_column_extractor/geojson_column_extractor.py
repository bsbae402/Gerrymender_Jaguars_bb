import geopandas
import geojson
import pandas
import copy

fname = "19902000_WI_Election_Data_with_2017_Wards.json"
gdf = geopandas.read_file(fname)

# OBJECTID, GEOID, MCD_FIPS, LSAD, NAME, COUSUBFP, CNTY_NAME, STR_WARDS, PERSONS, PERSONS18, geometry
only_with_chosen_columns = gdf[['OBJECTID', 'GEOID', 'MCD_FIPS', 'LSAD', 'NAME', 
    'COUSUBFP', 'CNTY_NAME', 'STR_WARDS', 'PERSONS', 'PERSONS18', 'geometry']]
#print(only_with_chosen_columns)

cloned = copy.deepcopy(only_with_chosen_columns)

out_fname = "WI_wards_2010.json"
cloned.to_file(out_fname, driver="GeoJSON")