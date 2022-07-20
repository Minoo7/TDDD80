package com.vinga129.savolax.base;

import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.ui.later.User;
import com.vinga129.savolax.ui.later.UserRepository;

public abstract class BaseViewModel extends ViewModel {

    protected User user;

    public BaseViewModel() {
        user = UserRepository.getINSTANCE().getUser();
    }

}
