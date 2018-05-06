import geopandas
import pandas
import json
from shapely.geometry import Polygon
import shapely.ops as ops
import pyproj
from functools import partial

def convertToProjection(poly):
    projected = ops.transform(
        partial(
            pyproj.transform,
            pyproj.Proj(init='EPSG:4326'),
            pyproj.Proj(
                proj='aea',
                lat1=poly.bounds[1],
                lat2=poly.bounds[3]
            )
        ),
        poly
    )
    return projected

precinct_geo_fname = "./WI_wards_2010_jaguars_v2.json"   
#precinct_geo_fname = "./NH_precincts_2010.json"
gdf = geopandas.read_file(precinct_geo_fname)
geoid_list = gdf["GEOID10"]

geom_polygons = gdf.geometry

adj_geoid_relations = []
for i in range(0, len(geom_polygons)):
    relation = {}
    relation["from"] = geoid_list[i]
    adj_poly_objs = []
    for j in range(0, len(geom_polygons)):
        adj_poly_obj = {}
        if i == j:
            continue
        if geom_polygons[i].intersects(geom_polygons[j]):
            poly1 = geom_polygons[i]
            poly2 = geom_polygons[j]
            union_poly = poly1.union(poly2)
            projected1 = convertToProjection(poly1)
            projected2 = convertToProjection(poly2)
            projected_u = convertToProjection(union_poly)
            contact_length = (projected1.length + projected2.length - projected_u.length) / 2.0
            adj_poly_obj["geoid"] = geoid_list[j]
            adj_poly_obj["contact_length"] = contact_length
            adj_poly_objs.append(adj_poly_obj)
    relation["to"] = adj_poly_objs
    adj_geoid_relations.append(relation)

out_relation_fname = "./WI_2010_precincts_neighbor_relations.json"
with open(out_relation_fname, 'w') as outfile:
    json.dump(adj_geoid_relations, outfile)
 
