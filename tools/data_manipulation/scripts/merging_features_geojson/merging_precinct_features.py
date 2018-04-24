import geopandas
import pandas
import json
import shapely.geometry
from shapely.geometry import Polygon
import geojson

state_code = "NH"
d_code_list = []
pgeo_dcode_mapping_file = "NH_pgeo_dcode.json"
mapping_list = json.load(open(pgeo_dcode_mapping_file))
for mapping in mapping_list:
    if mapping["dcode"] not in d_code_list:
        d_code_list.append(mapping["dcode"])



precinct_geo_fname = "NH_precincts_2010.json"
gdf = geopandas.read_file(precinct_geo_fname)
geoid_list = gdf["GEOID10"]
geometry_list = gdf.geometry

# for i in range(0, len(geometry_list)):
    


poly1 = shapely.geometry.asShape(gdf.geometry[0])
poly2 = shapely.geometry.asShape(gdf.geometry[1])
print(poly1)
print(poly2)

mergedPoly = poly1.union(poly2)

geojson_out = geojson.Feature(geometry=mergedPoly, properties={})
with open("merged_prac_out.json", 'w') as outfile:
    json.dump(geojson_out, outfile)
