import geopandas
import pandas
import json
import copy
from shapely.geometry import Polygon

fname = "./district_tl_2010_OH.json"

gdf = geopandas.read_file(fname)
print(gdf)

geom = gdf.geometry
#print(geom)

print("0 and 15? ", geom[0].intersects(geom[15]))
print("0 and 12? ", geom[0].intersects(geom[12]))
print("0 and 3? ", geom[0].intersects(geom[3]))
print("0 and 5? ", geom[0].intersects(geom[5]))
print("0 and 6? ", geom[0].intersects(geom[6]))
print("0 and 1? ", geom[0].intersects(geom[1]))
print("0 and 2? ", geom[0].intersects(geom[2]))
print("0 and 9? ", geom[0].intersects(geom[9]))
print("0 and 10? ", geom[0].intersects(geom[10]))

geoidList = gdf["GEOID10"]

adjList = []
for a in geom:
    adjList.append([])

for i in range(0, len(geom)):
    polyi = geom[i]
    for j in range(0, len(geom)):
        if i == j:
            continue
        polyj = geom[j]
        if polyi.intersects(polyj):
            adjList[i].append(j)
print(adjList)

geoidAdjList = []
for i in range(0, len(adjList)):
    geoidAdjList.append([])
    adjs = adjList[i]
    for adj in adjs:
        geoidAdjList[i].append(geoidList[adj])
print(geoidAdjList)

# YOU CANNOT ADD LIST AS AN ELEMENT OF 'Series'
# gdf["neighbors"] = pandas.Series(data=geoidAdjList, index=gdf.index)
# print(gdf)
# gdf.to_file("updated_district_2010_OH.json", driver="GeoJSON")

adjacent_geoid_json = []
for i in range(0, len(geoidAdjList)):
    oneDistrict = {}
    oneDistrict['from'] = geoidList[i]
    oneDistrict['to'] = geoidAdjList[i]
    adjacent_geoid_json.append(oneDistrict)
print(adjacent_geoid_json)

with open('neighbor_district_OH_2010.json', 'w') as outfile:
    json.dump(adjacent_geoid_json, outfile)