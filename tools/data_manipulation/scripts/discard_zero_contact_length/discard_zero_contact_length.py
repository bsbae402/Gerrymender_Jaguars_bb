import json

print("start")
# neighbor_fname = "NH_2010_precincts_neighbor_relations.json"
neighbor_fname = "WI_2010_precincts_neighbor_relations.json"

neighbor_list = json.load(open(neighbor_fname))
print("json load complete")
new_neighbor_list = []

for neighbor_relation in neighbor_list:
    target = neighbor_relation["from"]
    nei_precinct_props = neighbor_relation["to"]

    new_nei_precinct_props = []
    for one_nei_prop in nei_precinct_props:
        if one_nei_prop["contact_length"] == 0.0:
            print(one_nei_prop["geoid"] + " will be deleted at " + target)
        else :
            new_nei_precinct_props.append(one_nei_prop)

    new_neighbor_relation = {}
    new_neighbor_relation["from"] = target
    new_neighbor_relation["to"] = new_nei_precinct_props
    new_neighbor_list.append(new_neighbor_relation)

# out_fname ="NH_2010_precincts_neighbor_relations_v2.json"
out_fname ="WI_2010_precincts_neighbor_relations_v2.json"
with open(out_fname, 'w') as outfile:
    json.dump(new_neighbor_list, outfile)

print("asdfasdfasdfas")