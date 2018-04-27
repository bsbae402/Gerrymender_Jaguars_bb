import geopandas
import pandas

fname = "WI_wards_2010_simp80.json"
gdf = geopandas.read_file(fname)
gdf.rename(columns = {'GEOID':'GEOID10'}, inplace=True)
print(gdf)

out_fname = "WI_wards_2010_simp80_jaguars.json"
gdf.to_file(out_fname, driver = "GeoJSON")
