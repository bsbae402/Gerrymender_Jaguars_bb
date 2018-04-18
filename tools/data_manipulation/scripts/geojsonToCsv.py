import json
import csv

#open the files here
file = open('test.json')
csvfile = open('OHgeojsonPrecinctMapping.csv', 'w')
parsingJson = json.load(file)
data = parsingJson['mappings']

file.close()

file = csv.writer(csvfile, delimiter = ',', quotechar="'", quoting=csv.QUOTE_ALL)
for item in data:
	file.writerow(item.values())
csvfile.close()