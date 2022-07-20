package com.vinga129.savolax.base;

import androidx.databinding.BaseObservable;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.RestAPI;

public class NetworkBaseObservable extends BaseObservable {
    protected Controller controller;
    protected RestAPI restAPI;

    public NetworkBaseObservable() {
        controller = Controller.getInstance();
        restAPI = controller.getRestAPI();
    }
}
