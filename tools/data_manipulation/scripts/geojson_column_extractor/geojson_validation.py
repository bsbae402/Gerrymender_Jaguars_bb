import json

fname = "./WI_wards_2010.json"

with open(fname) as geojson_data:
    pydata = json.load(geojson_data)

print("It seems okay!")