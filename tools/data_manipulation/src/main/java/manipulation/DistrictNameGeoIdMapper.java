package manipulation;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DistrictNameGeoIdMapper {
    private class DistrictObjectFormat {
        public String cd111gp;
        public String geoId;
        public long aland10;

        public DistrictObjectFormat(String cd111gp, String geoId, long aland10) {
            this.cd111gp = cd111gp;
            this.geoId = geoId;
            this.aland10 = aland10;
        }
    }

    public void extractDistrictNameAndGeoId(String geoFilePath, String outPath) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
        Gson gsonOut = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create();
        List<DistrictObjectFormat> districtObjectFormats = new ArrayList<DistrictObjectFormat>();

        FileReader fileReader;
        try{
            fileReader = new FileReader(geoFilePath);

            FeatureCollection featureCollection = gson.fromJson(fileReader, FeatureCollection.class);
            List<Feature> featureList = featureCollection.features();
            for(Feature ft : featureList) {
                JsonElement cd111fp = ft.properties().get("CD111FP");
                JsonElement geoid10 = ft.properties().get("GEOID10");
                JsonElement aland10 = ft.properties().get("ALAND10");
                districtObjectFormats.add(new DistrictObjectFormat(
                        cd111fp.getAsString(),
                        geoid10.getAsString(),
                        aland10.getAsLong()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out;
        try {
            out = new PrintWriter(outPath);
            out.print(gsonOut.toJson(districtObjectFormats));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
