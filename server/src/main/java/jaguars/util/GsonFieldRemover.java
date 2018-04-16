package jaguars.util;

import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public final class GsonFieldRemover {
    public static void removeFieldsByClass(JsonElement jsonElement, Class elementClass, Class fieldClass) {
        List<Field> fields = Arrays.asList(elementClass.getDeclaredFields());
        for(Field field : fields) {
            if(field.getType().equals(fieldClass))
                jsonElement.getAsJsonObject().remove(field.getName());
        }
    }
}
