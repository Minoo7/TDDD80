package com.vinga129.savolax.base;

import static com.vinga129.savolax.util.HelperUtil.parseHttpError;
import static com.vinga129.savolax.util.HelperUtil.properFormValue;

import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vinga129.savolax.custom.CustomTextInputLayout;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.data.ResultHolder;
import io.reactivex.observers.DisposableSingleObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import retrofit2.HttpException;

public abstract class FormFragment<T, B extends ViewDataBinding> extends BaseFragment<B> {

    protected List<CustomTextInputLayout> formViews = new ArrayList<>();

    public T formDataToClass(ViewGroup container, Class<T> _class) throws IOException {
        if (formViews.isEmpty())
            setListOfInputLayouts(container);

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

    /**
     * Recursive function to add all CustomTextInputLayout views to formViews
     */
    private void setListOfInputLayouts(ViewGroup container) {
        int childViewCount = container.getChildCount();
        for (int i = 0; i < childViewCount; i++) {
            View view = container.getChildAt(i);
            if (view instanceof CustomTextInputLayout)
                formViews.add((CustomTextInputLayout) view);
            else if (view instanceof ViewGroup)
                setListOfInputLayouts((ViewGroup) view);
        }
    }

    @SuppressWarnings("ConstantConditions")
    /*
     * For every CustomTextInputLayout with the matching key returned,  in the errorMap from the server, set the
     * error returned from the server to the TextInputLayout.setError to display it on screen
     */
    protected void showErrors(Map<String, List<String>> errorMap) {

        formViews.forEach(this::clearError);
        formViews.stream().filter(inputLayout -> errorMap.containsKey(inputLayout.getKey()))
                .collect(Collectors.toSet())
                .forEach(inputLayout -> inputLayout.setError(String.join(",", (errorMap.get(inputLayout.getKey())))));
    }

    private void clearError(CustomTextInputLayout inputLayout) {
        inputLayout.setError("");
        inputLayout.setErrorEnabled(false);
    }

    /**
     * SingleObserver used by Form viewmodels to remove redundant repetition of error handler code
     */
    @SuppressWarnings({"rawtypes, unchecked"})
    public static <T> DisposableSingleObserver<Result> DefaultSingleFormObserver(
            MutableLiveData<ResultHolder<T>> liveData) {
        return new DisposableSingleObserver<Result>() {
            @Override
            public void onSuccess(final Result value) {
                liveData.setValue(new ResultHolder(((Success<?>) value).getData()));
            }

            @Override
            public void onError(final Throwable e) {
                if (e instanceof HttpException)
                    liveData.setValue(parseHttpError((HttpException) e));
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        formViews = new ArrayList<>();
    }
}
