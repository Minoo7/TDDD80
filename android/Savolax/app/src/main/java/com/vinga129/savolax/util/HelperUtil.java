package com.vinga129.savolax.util;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.vinga129.savolax.custom.CustomTextInputLayout;
import com.vinga129.savolax.data.Result.Error;
import com.vinga129.savolax.data.ResultHolder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    public static void makeMessage(Context context, View view, String text) {
        TSnackbar message = TSnackbar.make(view, text, TSnackbar.LENGTH_LONG);
        Paris.styleBuilder(message.getView()).backgroundTint(ContextCompat.getColor(context, R.color.message)).apply();
        message.show();
    }

    public static void makeWarning(Context context, View view, String text) {
        TSnackbar warning = TSnackbar.make(view, text, TSnackbar.LENGTH_LONG);
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

    public static <T> ResultHolder<T> parseHttpError(HttpException httpError) {
        String errorBody = null;
        try {
            errorBody = Objects
                    .requireNonNull(Objects.requireNonNull(httpError.response()).errorBody()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Error error = null;
        if (errorBody.startsWith("{")) {
            error = new Error(new Gson().fromJson(errorBody, Map.class));
            return new ResultHolder<>(error);
        } else {
            error = new Error(httpError.code(), errorBody);
            return new ResultHolder<>(error);
        }
    }

    public static File persistImage(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            System.out.println("Couldn't write bitmap");
            return null;
        }
    }

    /*public static MoveToProfile moveToProfileAction(int id) {
        CustomNullableIntegerArgument arg = new CustomNullableIntegerArgument(id);
        return MobileNavigationDirections.moveToProfile().setCustomerId(arg);
    }

    @SuppressWarnings("ConstantConditions")
    public static OnItemListener getDefaultOnMiniCustomerItemListener(NavController navController) {
        return (pos, data) -> {
            //navController.popBackStack();
            navController.navigate(moveToProfileAction(((MiniCustomer) data).getId()));
        };
    }*/
}
