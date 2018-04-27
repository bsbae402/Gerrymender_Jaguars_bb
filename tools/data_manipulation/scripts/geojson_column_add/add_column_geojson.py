import geopandas
import pandas

fname = "WI_Congressional_Districts_2011.json"
gdf = geopandas.read_file(fname)
# "properties":{"OBJECTID":7,"District_N":"7","GlobalID":"{657EE9D6-C010-4184-A6C7-0DB1C0E4BFD1}"}
district_n_list = gdf['District_N']
dnum_list = []
for district_n in district_n_list:
    if len(district_n) == 1:
        dnum_list.append('0' + district_n)
    else:
        dnum_list.append(district_n)

state_code = "WI"
state_geoid = "55"

d_geoid_list = []
d_code_list = []
for dnum in dnum_list:
    d_geoid_list.append(state_geoid + dnum)
    d_code_list.append(state_code + dnum)

print(d_geoid_list)
print(d_code_list)

d_geoid_series = pandas.Series(d_geoid_list)
gdf['GEOID10'] = d_geoid_series
print(gdf)

d_code_series = pandas.Series(d_code_list)
gdf['DCODE'] = d_code_series
print(gdf)

out_fname = "WI_district_2011_jaguars.json"
gdf.to_file(out_fname, driver = "GeoJSON")
