package com.vinga129.savolax.base;

import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.other.User;
import com.vinga129.savolax.other.UserRepository;

public abstract class BaseViewModel extends ViewModel {

    protected User user;

    public BaseViewModel() {
        user = UserRepository.getINSTANCE().getUser();
    }

}
