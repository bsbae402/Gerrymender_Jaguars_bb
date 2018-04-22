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

public class PrecinctNameGeoIdMapper {
    private class PrecinctObjectFormat {
        public String name;
        public String geoId;
        public String vtdst10;
        public int aland10;
        public String countyfp10;
        public int awater10;

        public PrecinctObjectFormat(String name, String geoId, String vtdst10, int aland10, String countyfp10, int awater10) {
            this.name = name;
            this.geoId = geoId;
            this.vtdst10 = vtdst10;
            this.aland10 = aland10;
            this.countyfp10 = countyfp10;
            this.awater10 = awater10;
        }
    }

    public void extractPrecinctNameAndGeoId(String geoFilePath, String outPath) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
        Gson gsonOut = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create();
        List<PrecinctObjectFormat> precinctObjectFormats = new ArrayList<PrecinctObjectFormat>();

        FileReader fileReader;
        try{
            fileReader = new FileReader(geoFilePath);

            FeatureCollection featureCollection = gson.fromJson(fileReader, FeatureCollection.class);
            List<Feature> featureList = featureCollection.features();
            for(Feature ft : featureList) {
                JsonElement name10 = ft.properties().get("NAME10");
                JsonElement geoid10 = ft.properties().get("GEOID10");
                JsonElement vtdst10 = ft.properties().get("VTDST10");
                JsonElement aland10 = ft.properties().get("ALAND10");
                JsonElement countyfp10 = ft.properties().get("COUNTYFP10");
                JsonElement awater10 = ft.properties().get("AWATER10");
                precinctObjectFormats.add(new PrecinctObjectFormat(
                        name10.getAsString(),
                        geoid10.getAsString(),
                        vtdst10.getAsString(),
                        aland10.getAsInt(),
                        countyfp10.getAsString(),
                        awater10.getAsInt()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out;
        try {
            out = new PrintWriter(outPath);
            out.print(gsonOut.toJson(precinctObjectFormats));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
