import geopandas
import pandas
import json
import shapely.geometry
from shapely.geometry import Polygon
import geojson

precinct_geo_fname = "./district_tl_2010_NH.json"
gdf = geopandas.read_file(precinct_geo_fname)
geoid_list = gdf["GEOID10"]

poly1 = shapely.geometry.asShape(gdf.geometry[0])
poly2 = shapely.geometry.asShape(gdf.geometry[1])
print(poly1)
print(poly2)

mergedPoly = poly1.union(poly2)

geojson_out = geojson.Feature(geometry=mergedPoly, properties={})
with open("merged_prac_out.json", 'w') as outfile:
    json.dump(geojson_out, outfile)
