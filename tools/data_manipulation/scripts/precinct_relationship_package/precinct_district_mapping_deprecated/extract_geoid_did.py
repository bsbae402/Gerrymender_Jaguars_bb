import pandas
import json

# This data doesn't have a precinct "33015ZZZZZZ"
# Manually add the record at the end; it is in NH01
fname = "NHgeojsonPrecinctMapping.csv"
df = pandas.read_csv(fname) # DataFrame
#print(df)

#print(df['geoid'])

mapping_list = []
for i in range(0, len(df['geoid'])):
    mapping = {}
    mapping['pgeoid'] = df['geoid'][i]
    mapping['dcode'] = df['districtID'][i]
    mapping_list.append(mapping)

out_fname = "NH_pgeo_dcode.json"
with open(out_fname, 'w') as outfile:
    json.dump(mapping_list, outfile)