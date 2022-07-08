package com.vinga129.savolax.retrofit;

import android.app.Application;

import android.view.View;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.data.Result;

public abstract class NetworkViewModel extends ViewModel {
    protected Controller controller;
    protected RestAPI restAPI;

    public NetworkViewModel() {
        controller = Controller.getInstance();
        restAPI = controller.getRestAPI();
    }
}

