package com.vinga129.savolax.base;

import static com.vinga129.savolax.util.HelperUtil.properFormValue;

import androidx.databinding.ViewDataBinding;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vinga129.savolax.custom.CustomTextInputLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class FormFragment<T, B extends ViewDataBinding> extends BaseFragment<B> {

    protected List<CustomTextInputLayout> formViews = new ArrayList<>();

    public T formDataToClass(Class<T> _class) throws IOException {
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

    public JsonObject saveFormData() throws IOException {
        JsonObject formData = new JsonObject();
        for (CustomTextInputLayout formView : formViews) {
            JsonElement value = properFormValue(formView);;
            formData.add(formView.getKey(), value);
        }
        return formData;
    }

    protected void showErrors(Map<String, List<String>> errorMap) {
        formViews.stream().filter(field -> errorMap.containsKey(field.getKey())).collect(Collectors.toSet())
                .forEach(f -> Objects.requireNonNull(f.getEditText())
                        .setError(String.join(",", Objects.requireNonNull(errorMap.get(f.getKey())))));
    }

    /*protected void recoverSave(Map<String, String> formData) {
        formViews.stream().filter(field -> formData.containsKey(field.getKey())).collect(Collectors.toSet())
                .forEach(f -> Objects.requireNonNull(f.getEditText())
                        .setText(Objects.requireNonNull(formData.get(f.getKey()))));
    }*/

    protected void restoreFormData(JsonObject formData) {
        System.out.println("HEJEJEJEJ");
        System.out.println(formData);
    }
}
