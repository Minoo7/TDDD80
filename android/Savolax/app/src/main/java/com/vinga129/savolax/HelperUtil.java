package com.vinga129.savolax;

import android.content.ClipData.Item;
import android.content.Context;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.airbnb.paris.Paris;
import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.vinga129.savolax.custom.CustomTextInputLayout;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.BusinessTypes;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

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
        if (view instanceof CustomTextInputLayout) {
            return Objects.requireNonNull(((CustomTextInputLayout) view).getEditText()).getText().toString();
        }
        return null;
    }

    public static JsonElement properFormValue(CustomTextInputLayout view) {
        String string = getEditText(view).trim();
        String properValue = string.equals("") ? null : string;
        return new Gson().toJsonTree(properValue);
    }

    public static void makeWarning(Context context, View view, String message) {
        TSnackbar warning = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG);
        Paris.styleBuilder(warning.getView()).backgroundTint(ContextCompat.getColor(context, R.color.warning))
                .apply();
        warning.show();
    }
}
