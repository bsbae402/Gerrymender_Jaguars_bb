import geopandas
import pandas
import json
import copy
from shapely.geometry import Polygon

precinct_geo_fname = "./NH_precincts_2010.json"
gdf = geopandas.read_file(precinct_geo_fname)
geoid_list = gdf["GEOID10"]

geom_polygons = gdf.geometry

adj_geoid_relations = []
for i in range(0, len(geom_polygons)):
    relation = {}
    relation["from"] = geoid_list[i]
    adj_polys = []
    for j in range(0, len(geom_polygons)):
        if i == j:
            continue
        if geom_polygons[i].intersects(geom_polygons[j]):
            adj_polys.append(geoid_list[j])
    relation["to"] = adj_polys
    adj_geoid_relations.append(relation)

out_relation_fname = "./NH_2010_precincts_neighbors.json"
with open(out_relation_fname, 'w') as outfile:
    json.dump(adj_geoid_relations, outfile)