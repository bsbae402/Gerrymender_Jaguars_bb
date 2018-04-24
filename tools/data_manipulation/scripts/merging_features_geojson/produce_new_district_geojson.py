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

geojson_feature_list = []
for dist_i in range(0, len(district_geoid_list)):
    dgeoid = district_geoid_list[dist_i]
    dcode = state_code + district_cd111fp_list[dist_i]
    # find precinct geoids that are in district[dist_i]
    prec_geoid_list = []
    for mapping in mapping_list:
        if mapping["dcode"] == dcode:
            prec_geoid_list.append(mapping["pgeoid"])
    # find precinct polygons of district index = dist_i
    prec_poly_list = []
    for prec_geoid in prec_geoid_list:
        for idx in range(0, len(pfile_geoid_list)):
            if prec_geoid == pfile_geoid_list[idx]:
                prec_poly_list.append(pfile_gdf.geometry[idx])
    # produce aggregate of the precinct polygons
    poly_aggregate = prec_poly_list[0]
    for i in range(1, len(prec_poly_list)):
        poly_aggregate = poly_aggregate.union(prec_poly_list[i])

    geojson_feature = geojson.Feature(
        geometry=poly_aggregate, 
        properties={
            "STATEFP10": district_gdf["STATEFP10"][dist_i],
            "CD111FP":district_gdf["CD111FP"][dist_i],
            "GEOID10":district_gdf["GEOID10"][dist_i],
            "NAMELSAD10":district_gdf["NAMELSAD10"][dist_i],
            "ALAND10":district_gdf["ALAND10"][dist_i].item(),
            "AWATER10":district_gdf["AWATER10"][dist_i].item()
        })
    geojson_feature_list.append(geojson_feature)

feature_collection = geojson.FeatureCollection(geojson_feature_list)

new_district_geo_fname = "district_2010_NH_jaguars.json"
with open(new_district_geo_fname, 'w') as outfile:
    json.dump(feature_collection, outfile)

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