package com.vinga129.savolax.base;

import androidx.databinding.ViewDataBinding;
import com.vinga129.savolax.data.UserView;
import java.io.IOException;

public abstract class FormFragment<T, B extends ViewDataBinding> extends BaseFragment<B> {

    public abstract T addFormData() throws IOException;

    // public abstract void updateUi(UserView model);

    public abstract void showFail();
}
