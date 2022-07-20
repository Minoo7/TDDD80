package com.vinga129.savolax.base;

import android.app.Application;

import android.view.View;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.RestAPI;

public abstract class NetworkViewModel extends BaseViewModel {
    protected Controller controller;
    protected RestAPI restAPI;

    public NetworkViewModel() {
        controller = Controller.getInstance();
        restAPI = controller.getRestAPI();
    }
}

