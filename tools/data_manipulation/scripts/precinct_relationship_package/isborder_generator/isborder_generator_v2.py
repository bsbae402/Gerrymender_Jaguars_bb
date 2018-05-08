import geopandas
import pandas
import json
import copy
from shapely.geometry import Polygon

neighbor_fname = "OH_2010_precincts_neighbor_relations_v2.json"
mapping_fname = "precinct_district_mapping_OH.json"
# neighbor_fname = "WI_2010_precincts_neighbor_relations_v2.json"
# mapping_fname = "precinct_district_mapping_WI.json"
# neighbor_fname = "NH_2010_precincts_neighbor_relations_v2.json"
# mapping_fname = "precinct_district_mapping_NH.json"

neighbor_list = json.load(open(neighbor_fname))
mapping_list = json.load(open(mapping_fname))

print(len(neighbor_list))
print(len(mapping_list))

# create a dictionary for more efficiency in searching
dgeoid_list = []
for mapping in mapping_list:
    if mapping["dgeoid"] not in dgeoid_list:
        dgeoid_list.append(mapping["dgeoid"])
print(dgeoid_list)
district_to_precincts = {}
for dgeoid in dgeoid_list:
    district_to_precincts[dgeoid] = []
for mapping in mapping_list:
    district_to_precincts[mapping["dgeoid"]].append(mapping["pgeoid"])
print(district_to_precincts)

# find borderness of each precinct
isborder_dict_list = []
for neighbor_relation in neighbor_list:
    isborder_dict = {} # { pgeoid: ...; border: ...}
    target = neighbor_relation["from"]
    nei_precinct_props = neighbor_relation["to"]

    isborder_dict["pgeoid"] = target
    target_distr = "" # current target's affiliation
    for dgeoid in dgeoid_list:
        if target in district_to_precincts[dgeoid]:
            target_distr = dgeoid
            break
    isborder_dict["border"] = False
    for one_nei_prop in nei_precinct_props:
        one_nei_geoid = one_nei_prop["geoid"]
        if one_nei_geoid not in district_to_precincts[target_distr]:
            isborder_dict["border"] = True
            break
    isborder_dict_list.append(isborder_dict)

# out_fname = "isborder_NH_2010_v2.json"
# out_fname = "isborder_WI_2010_v2.json"
out_fname = "isborder_OH_2010_v2.json"
with open(out_fname, 'w') as outfile:
    json.dump(isborder_dict_list, outfile)
