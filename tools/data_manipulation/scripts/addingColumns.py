import pandas as pd
import numpy as np

#reading the file
mainColumns = ['name', 'geoid', 'vtdst10', 'aland', 'awater', 'code', 'county']
extractColumns = ['code', 'county']
mainCsv = pd.read_csv('OHgeojsonPrecinctMapping.csv', names=mainColumns)
extractCsv = pd.read_csv('oh_county_mapping.csv', names=extractColumns)

#extracting the county codes
mainCode = mainCsv.code.tolist()
countyCode = extractCsv.code.tolist()
#writing the county names
countyName = extractCsv.county.tolist()
writeCounty = mainCsv.county.tolist()

#looping through all of the counties to match them up properly
for x in mainCode:
	for y in countyCode:
		if(x == y):
			readDF = pd.DataFrame(countyName)
			writeDF = pd.DataFrame(writeCounty)
			try:
				print(readDF.iloc[int(y)])
				break
			except ValueError:	
				print ("will only happen for the first column.")