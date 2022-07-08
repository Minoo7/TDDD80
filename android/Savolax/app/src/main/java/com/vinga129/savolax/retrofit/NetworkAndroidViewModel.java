package com.vinga129.savolax.retrofit;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

public abstract class NetworkAndroidViewModel extends AndroidViewModel {

    public Controller controller;
    public RestAPI restAPI;

    public NetworkAndroidViewModel(Application application) {
        super(application);

        controller = Controller.getInstance();
        restAPI = controller.getRestAPI();
    }

    //public abstract void loadData();
}
