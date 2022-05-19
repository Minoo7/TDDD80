package com.vinga129.savolax;

import android.view.View;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class HelperUtil {

    public static class CustomMapSerializer implements JsonSerializer<Map<String, String>> {

        @Override
        public JsonElement serialize(Map<String, String> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            //Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            //        .enableComplexMapKeySerialization().setPrettyPrinting().create();
            Gson gson = new Gson();
            for (Entry<String, String> entry : src.entrySet()) {
                String snake_case = entry.getKey().replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
                snake_case = snake_case.replaceAll(" \\*", "");
                json.add(gson.toJsonTree(snake_case).getAsString(), gson.toJsonTree(entry.getValue()));
                /*System.out.println("entry.getKey(): " + entry.getKey());
                System.out.println("gson.toJson(): " + gson.toJson(entry.getKey()));
                json.add(gson.toJson(entry.getKey()), gson.toJsonTree(entry.getValue()));*/
            }

            return json;
        }
    }

    public static String getEditText(View view) {
        if (view instanceof TextInputLayout) {
            return Objects.requireNonNull(((TextInputLayout) view).getEditText()).getText().toString();
        }
        return null;
    }

    public static JsonElement properFormValue(View view) {
        String string = getEditText(view);
        String properValue = string.equals("") ? null : string;
        return new Gson().toJsonTree(properValue);
    }
}
