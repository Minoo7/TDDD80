package com.vinga129.savolax.base;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.RestAPI;
import com.vinga129.savolax.other.User;
import com.vinga129.savolax.other.UserRepository;

public abstract class NetworkAndroidViewModel extends AndroidViewModel {

    protected Controller controller;
    protected RestAPI restAPI;
    protected User user;

    public NetworkAndroidViewModel(Application application) {
        super(application);

        controller = Controller.getInstance();
        restAPI = controller.getRestAPI();
        user = UserRepository.INSTANCE.getUser();
    }
}
