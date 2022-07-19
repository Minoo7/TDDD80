package com.vinga129.savolax.retrofit;

import androidx.databinding.BaseObservable;

public class NetworkBaseObservable extends BaseObservable {
    protected Controller controller;
    protected RestAPI restAPI;

    public NetworkBaseObservable() {
        controller = Controller.getInstance();
        restAPI = controller.getRestAPI();
    }
}
