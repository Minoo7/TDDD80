package com.vinga129.savolax.util;

import android.content.Context;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.airbnb.paris.Paris;
import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vinga129.savolax.R;
import com.vinga129.savolax.ResultHolder;
import com.vinga129.savolax.custom.CustomTextInputLayout;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import retrofit2.HttpException;

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

    public static void clearError(CustomTextInputLayout textInputLayout) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
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

    public static <T> T addFormData(List<CustomTextInputLayout> formViews, Class<T> _class) throws IOException {
        JsonObject formData = new JsonObject();

        for (CustomTextInputLayout formView : formViews) {
            JsonElement value = properFormValue(formView);
            if (value.isJsonNull() && formView.isRequired())
                throw new IOException("Required fields can not be empty");
            formData.add(formView.getKey(), value);
        }

        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(formData), _class);
    }

    public static <T> ResultHolder<T> parseHttpError(HttpException httpError) throws IOException {
        String errorBody = Objects
                .requireNonNull(Objects.requireNonNull(httpError.response()).errorBody()).string();

        if (errorBody.startsWith("{")) {
            return new ResultHolder<>(new Gson().fromJson(errorBody, Map.class));
        }
        else {
            return  new ResultHolder<>(httpError.code(), errorBody);
        }
    }

    /*public static void handleHttpError(HttpException error, MutableLiveData<ResultHolder<?>> result, Context context, View view) {
        try {
            String errorBody = Objects
                    .requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string();

            if (errorBody.startsWith("{")) {
                result.setValue(new ResultHolder<>(new Gson().fromJson(errorBody, Map.class)));
            }
            else {
                makeWarning(context, view, errorBody);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }*/
}
