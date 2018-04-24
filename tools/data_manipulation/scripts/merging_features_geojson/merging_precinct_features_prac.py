import geopandas
import pandas
import json
import shapely.geometry
from shapely.geometry import Polygon
import geojson

state_code = "NH"
pgeo_dcode_mapping_file = "NH_pgeo_dcode.json"
mapping_list = json.load(open(pgeo_dcode_mapping_file))
    
district_geo_fname = "district_tl_2010_NH.json"
district_gdf = geopandas.read_file(district_geo_fname)
district_geoid_list = district_gdf["GEOID10"]
district_cd111fp_list = district_gdf["CD111FP"] # 01, 02, 03, ... randomly

precinct_geo_fname = "NH_precincts_2010.json"
pfile_gdf = geopandas.read_file(precinct_geo_fname)
pfile_geoid_list = pfile_gdf["GEOID10"]

# find precinct polygons of district index = 0
dgeoid = district_geoid_list[0]
dcode = state_code + district_cd111fp_list[0]
prec_geoid_list = []
for mapping in mapping_list:
    if mapping["dcode"] == dcode:
        prec_geoid_list.append(mapping["pgeoid"])
prec_poly_list = []
for prec_geoid in prec_geoid_list:
    for idx in range(0, len(pfile_geoid_list)):
        if prec_geoid == pfile_geoid_list[idx]:
            prec_poly_list.append(pfile_gdf.geometry[idx])

poly_aggregate = prec_poly_list[0]
for i in range(1, len(prec_poly_list)):
    poly_aggregate = poly_aggregate.union(prec_poly_list[i])

geojson_out = geojson.Feature(
    geometry=poly_aggregate, 
    properties={
        "STATEFP10": district_gdf["STATEFP10"][0],
        "CD111FP":district_gdf["CD111FP"][0],
        "GEOID10":district_gdf["GEOID10"][0],
        "NAMELSAD10":district_gdf["NAMELSAD10"][0],
        "ALAND10":district_gdf["ALAND10"][0].item(),
        "AWATER10":district_gdf["AWATER10"][0].item()
    })
with open("first_merged.json", 'w') as outfile:
    json.dump(geojson_out, outfile)

# "properties":{
#     "STATEFP10":"33",
#     "CD111FP":"01",
#     "GEOID10":"3301",
#     "NAMELSAD10":"Congressional District 1",
#     "LSAD10":"C2","CDSESSN":"111",
#     "MTFCC10":"G5200",
#     "FUNCSTAT10":"N",
#     "ALAND10":6333155788,"AWATER10":633153772,
#     "INTPTLAT10":"+43.4387313",
#     "INTPTLON10":"-071.1872601"
# }