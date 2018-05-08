import json
import geopandas

fname = "district_2010_NH_jaguars.json"
gdf = geopandas.read_file(fname)
# print(gdf.geometry[0].area)

dcode_to_dgeo = {} 
dcode_to_dgeo['NH01'] = '3301'
dcode_to_dgeo['NH02'] = '3302'

pgeo_dcode_fname = "NH_pgeo_dcode.json"
pgeo_dcode_list = json.load(open(pgeo_dcode_fname))

pgeo_dgeo_list = []
for pgeo_dcode in pgeo_dcode_list:
    pgeo_dgeo = {}
    pgeo_dgeo['pgeoid'] = pgeo_dcode['pgeoid']
    dgeoid = dcode_to_dgeo[pgeo_dcode['dcode']]
    pgeo_dgeo['dgeoid'] = dgeoid
    pgeo_dgeo_list.append(pgeo_dgeo)

with open('precinct_district_mapping_NH.json', 'w') as outfile:
    json.dump(pgeo_dgeo_list, outfile)
