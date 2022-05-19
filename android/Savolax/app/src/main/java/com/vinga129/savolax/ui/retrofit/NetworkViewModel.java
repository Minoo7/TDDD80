package com.vinga129.savolax.ui.retrofit;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public abstract class NetworkViewModel extends AndroidViewModel {
    public Controller controller;
    public RestAPI restAPI;

    public NetworkViewModel(Application application) {
        super(application);

        controller = Controller.getInstance();
        restAPI = controller.getRestAPI();
    }

    //public abstract void loadData();
}
