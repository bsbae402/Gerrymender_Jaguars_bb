import geopandas
import pandas
import json
import copy
from shapely.geometry import Polygon

neighbor_fname = "NH_2010_precincts_neighbors.json"
mapping_fname = "NH_pgeo_dcode.json"

neighbor_list = json.load(open(neighbor_fname))
mapping_list = json.load(open(mapping_fname))

print(len(neighbor_list))
# DIDNT FIND PROBLEM
# for i in range(0, len(neighbor_list)-1):
#     for j in range(i+1, len(neighbor_list)):
#         if(neighbor_list[i]["from"] == neighbor_list[j]["from"]):
#             print("duplicate!")
print(len(mapping_list))

# onemore = []
# for neighbor in neighbor_list:
#     onemore.append(neighbor["from"])
# oneless = []
# for mapping in mapping_list:
#     oneless.append(mapping["pgeoid"])
# for target in onemore:
#     if target not in oneless:
#         print(target, " is not in!")

# create a dictionary for more efficiency in searching
dcode_list = []
for mapping in mapping_list:
    if mapping["dcode"] not in dcode_list:
        dcode_list.append(mapping["dcode"])
district_to_precincts = {}
for dcode in dcode_list:
    district_to_precincts[dcode] = []
for mapping in mapping_list:
    district_to_precincts[mapping["dcode"]].append(mapping["pgeoid"])

isborder_dict_list = []
for neighbor in neighbor_list:
    isborder_dict = {}
    target = neighbor["from"]
    nei_precincts = neighbor["to"]
    isborder_dict["pgeoid"] = target
    targets_dist = ""
    for dcode in dcode_list:
        if target in district_to_precincts[dcode]:
            targets_dist = dcode
            break
    isborder_dict["border"] = False
    for one_nei in nei_precincts:
        if one_nei not in district_to_precincts[targets_dist]:
            isborder_dict["border"] = True
            break
    isborder_dict_list.append(isborder_dict)

out_fname = "isborder_NH_2010.json"
with open(out_fname, 'w') as outfile:
    json.dump(isborder_dict_list, outfile)


