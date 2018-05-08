package jaguars.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import jaguars.AppConstants;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

@Service
public class PropertiesManager {
    private class PropertiesTable {
        @SerializedName(AppConstants.PROP_NAME_COMPACTNESS_WEIGHT_PP)
        public double defaultCompactnessWeightPp;
        @SerializedName(AppConstants.PROP_NAME_COMPACTNESS_WEIGHT_SCH)
        public double defaultCompactnessWeightSch;
        @SerializedName(AppConstants.PROP_NAME_EFFICIENCY_WEIGHT)
        public double defaultEfficiencyWeight;
        @SerializedName(AppConstants.PROP_NAME_POPULATION_THRESHOLD)
        public double defaultPopulationThreshold;
        @SerializedName(AppConstants.PROP_NAME_MAX_LOOP_STEPS)
        public int maxLoopSteps;

        public PropertiesTable(double defaultCompactnessWeightPp, double defaultCompactnessWeightSch,
                               double defaultEfficiencyWeight, double defaultPopulationThreshold, int maxLoopSteps) {
            this.defaultCompactnessWeightPp = defaultCompactnessWeightPp;
            this.defaultCompactnessWeightSch = defaultCompactnessWeightSch;
            this.defaultEfficiencyWeight = defaultEfficiencyWeight;
            this.defaultPopulationThreshold = defaultPopulationThreshold;
            this.maxLoopSteps = maxLoopSteps;
        }
    }

    private PropertiesTable propertiesTable;

    public PropertiesManager() {
        String propertiesFilePath = AppConstants.PATH_APP_PROPERTIES_JSON;
        try{
            FileReader fileReader = new FileReader(propertiesFilePath);
            Gson gson = new GsonBuilder().create();
            propertiesTable = gson.fromJson(fileReader, PropertiesTable.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getDefaultCompactnessWeightPp() {
        return propertiesTable.defaultCompactnessWeightPp;
    }

    public double getDefaultCompactnessWeightSch() {
        return propertiesTable.defaultCompactnessWeightSch;
    }

    public double getDefaultEfficiencyWeight() {
        return propertiesTable.defaultEfficiencyWeight;
    }

    public double getDefaultPopulationThreshold() {
        return propertiesTable.defaultPopulationThreshold;
    }

    public int getMaxLoopSteps() {
        return propertiesTable.maxLoopSteps;
    }

    public void setDefaultCompactnessWeightPp(double defaultCompactnessWeightPp) {
        this.propertiesTable.defaultCompactnessWeightPp = defaultCompactnessWeightPp;
    }

    public void setDefaultCompactnessWeightSch(double defaultCompactnessWeightSch) {
        this.propertiesTable.defaultCompactnessWeightSch = defaultCompactnessWeightSch;
    }

    public void setDefaultEfficiencyWeight(double defaultEfficiencyWeight) {
        this.propertiesTable.defaultEfficiencyWeight = defaultEfficiencyWeight;
    }

    public void setDefaultPopulationThreshold(double defaultPopulationThreshold) {
        this.propertiesTable.defaultPopulationThreshold = defaultPopulationThreshold;
    }

    public void setMaxLoopSteps(int maxLoopSteps) {
        this.propertiesTable.maxLoopSteps = maxLoopSteps;
    }

    public boolean updateAlgorithmContraints(double compactnessWeightPp, double compactnessWeightSch, double efficiencyWeight,
                                             double populationThreshold, int maxLoops) {
        PropertiesTable newProperties = new PropertiesTable(compactnessWeightPp, compactnessWeightSch,
                efficiencyWeight, populationThreshold, maxLoops);
        if(exportUpdatedProperties(newProperties)) {
            // file writing successful
            propertiesTable = newProperties;
            return true;
        }
        else {
            return false;
        }
    }

    private boolean exportUpdatedProperties(PropertiesTable newProperties) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = new FileWriter(AppConstants.PATH_APP_PROPERTIES_JSON);
            gson.toJson(newProperties, writer);
            writer.flush(); // don't forget to flush
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

/*
{
  "default_compactness_weight": 0.5,
  "default_efficiency_weight": 0.5,
  "default_population_threshold": 0.1,
  "max_loop_steps": 10000
}
*/
