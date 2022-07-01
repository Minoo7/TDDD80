package com.vinga129.savolax.base;

import static com.vinga129.savolax.HelperUtil.makeWarning;
import static com.vinga129.savolax.HelperUtil.properFormValue;

import android.content.Context;
import android.view.View;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vinga129.savolax.custom.CustomTextInputLayout;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.UserView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import retrofit2.HttpException;

public abstract class FormFragment<T, B extends ViewDataBinding> extends BaseFragment<B> {
    protected List<CustomTextInputLayout> formViews = new ArrayList<>();

    // public abstract T addFormData() throws IOException;

    public T addFormData(Class<T> _class) throws IOException {
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

    // public abstract void updateUi(UserView model);

    public abstract void showFail();
}
