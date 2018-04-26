import geopandas
import copy
import numpy

state_code = "OH"
state_number = "39"
fname = "./OH_CD_2011_REVISED.json"
gdf = geopandas.read_file(fname)
district_number_list = gdf['DISTRICT']
district_code_list = []
district_geoid_list = []
for district_number in district_number_list:
    district_code_list.append(state_code + district_number)
    district_geoid_list.append(state_number + district_number)

gdf_clone = copy.deepcopy(gdf)
gdf_clone = gdf_clone.assign(GEOID10 = numpy.array(district_geoid_list))
gdf_clone = gdf_clone.assign(DCODE = numpy.array(district_code_list))

print(gdf_clone)
gdf_clone.to_file("OH_districts_2011_REVISED.json", driver="GeoJSON")