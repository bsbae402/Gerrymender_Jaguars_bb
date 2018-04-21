import geopandas
import shapely.ops as ops
from functools import partial
import pyproj
import json

fname = "state_tl_2010_NH.json"
# fname = "district_tl_2010_NH.json"
# fname = "NH_precincts.json"
gdf = geopandas.read_file(fname)
print(gdf)

jsonObjList = []
for i in range(0, len(gdf.geometry)):
    geoid_area_perimeter = {}
    polygon = gdf.geometry[i]
    geoid = gdf.GEOID10[i]
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
    geoid_area_perimeter['geoid'] = geoid
    geoid_area_perimeter['area'] = projected.area
    geoid_area_perimeter['perimeter'] = projected.length
    jsonObjList.append(geoid_area_perimeter)

with open('area_perimeter_state_NH_2010.json', 'w') as outfile:
    json.dump(jsonObjList, outfile)
