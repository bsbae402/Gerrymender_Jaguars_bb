import geopandas
import pandas
import sys
import json

district_fname = "OH_districts_2011_REVISED.json"
district_gdf = geopandas.read_file(district_fname)

precinct_fname = "OH_precincts_fixed.json"
precinct_gdf = geopandas.read_file(precinct_fname)

pgeoid_list = precinct_gdf['GEOID10']
dgeoid_list = district_gdf['GEOID10']

pidx_to_intr_didxs = []
for p_idx in range(0, len(precinct_gdf.geometry)):
    precinct_poly = precinct_gdf.geometry[p_idx]
    intr_didxs = []
    for d_idx in range(0, len(district_gdf.geometry)):
        district_poly = district_gdf.geometry[d_idx]
        intr_poly = precinct_poly.intersection(district_poly)
        if intr_poly.area > 0 :
            intr_didxs.append(d_idx)
    pidx_to_intr_didxs.append(intr_didxs)
# print(pidx_to_intr_didxs)

# build json array for affiliation mapping
mapping_json_arr = []
for p_idx in range(0, len(pidx_to_intr_didxs)):
    pgeoid = pgeoid_list[p_idx]
    didxs = pidx_to_intr_didxs[p_idx]
    mapping = {}
    if len(didxs) > 1:
        precinct_poly = precinct_gdf.geometry[p_idx]
        max_intr_area_didx = didxs[0]
        district_poly = district_gdf.geometry[max_intr_area_didx]
        intr_poly = precinct_poly.intersection(district_poly)
        current_max_intr_area = intr_poly.area
        for i in range(1, len(didxs)):
            competing_didx = didxs[i]
            district_poly = district_gdf.geometry[competing_didx]
            intr_poly = precinct_poly.intersection(district_poly)
            if current_max_intr_area < intr_poly.area :
                max_intr_area_didx = competing_didx
                current_max_intr_area = intr_poly.area
        # we have find max area intr didx
        mapping["pgeoid"] = pgeoid
        mapping["dgeoid"] = dgeoid_list[max_intr_area_didx]
    else :
        mapping["pgeoid"] = pgeoid
        mapping["dgeoid"] = dgeoid_list[didxs[0]]
    mapping_json_arr.append(mapping)

with open('precinct_district_mapping_OH.json', 'w') as outfile:
    json.dump(mapping_json_arr, outfile)
