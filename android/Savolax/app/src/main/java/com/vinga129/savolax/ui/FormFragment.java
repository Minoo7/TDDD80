package com.vinga129.savolax.ui;

import androidx.databinding.ViewDataBinding;

import com.vinga129.savolax.data.UserView;

public abstract class FormFragment<T extends Object, C extends ViewDataBinding> extends BaseFragment2<C> {
    public abstract T getFormData();
    public abstract void updateUi(UserView model);
    public abstract void showFail();
}
