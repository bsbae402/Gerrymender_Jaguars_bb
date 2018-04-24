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
print("projected compactness: " + str(pp_measure))

print(geom_projected.length)
print(geom.length)
unprojected_measure = 4 * math.pi * geom.area / (geom.length ** 2)
print("unprojected compactness: " + str(unprojected_measure))

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
    #print(gdf2.GEOID10[i] + ": " + str(compactness))


# python3 program to evaluate
# area of a polygon using
# shoelace formula
 
# (X[i], Y[i]) are coordinates of i'th point.
def polygonArea(X, Y, n):
 
    # Initialze area
    area = 0.0
 
    # Calculate value of shoelace formula
    j = n - 1
    for i in range(0,n):
        area += (X[j] + X[i]) * (Y[j] - Y[i])
        j = i   # j is previous vertex to i
     
 
    # Return absolute value
    return abs(area / 2.0)
 
# Driver program to test above function
X = [0, 2, 4]
Y = [1, 3, 7]
n = len(X)
print(polygonArea(X, Y, n))
 
# This code is contributed by
# Smitha Dinesh Semwal