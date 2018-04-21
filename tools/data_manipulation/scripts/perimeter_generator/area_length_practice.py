import geopandas
import pandas
import json
from shapely.geometry import Polygon
import shapely.ops as ops
import pyproj
from functools import partial
import math

fname = "state_tl_2010_NH.json"
gdf = geopandas.read_file(fname)
print(gdf.geometry[0].area)

# object.bounds
#   Returns a (minx, miny, maxx, maxy) tuple (float values) that bounds the object.
print(gdf.geometry[0].bounds)
print(gdf.geometry[0].bounds[1])
print(gdf.geometry[0].bounds[3])
geom = gdf.geometry[0]
geom_projected = ops.transform(
    partial(
        pyproj.transform,
        pyproj.Proj(init='EPSG:4326'),
        pyproj.Proj(
            proj='aea',
            lat1=geom.bounds[1],
            lat2=geom.bounds[3]
        )
    ), 
    geom
)
print(geom_projected.area) # unit is m^2
pp_measure = 4 * math.pi * geom_projected.area / (geom_projected.length ** 2)
print(pp_measure)

print(geom_projected.length)
print(geom.length)

fname2 = "NH_precincts.json"
gdf2 = geopandas.read_file(fname2)
for i in range(0, len(gdf2.geometry)):
    polygon = gdf2.geometry[i]
    projected = ops.transform(
        partial(
            pyproj.transform,
            pyproj.Proj(init='EPSG:4326'),
            pyproj.Proj(
                proj='aea',
                lat1=polygon.bounds[1],
                lat2=polygon.bounds[3]
            )
        ),
        polygon
    )
    compactness = 4 * math.pi * projected.area / (projected.length ** 2)
    print(gdf2.GEOID10[i] + ": " + str(compactness))
    print(projected.length)
