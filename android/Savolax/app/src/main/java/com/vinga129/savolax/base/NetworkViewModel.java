package com.vinga129.savolax.base;

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

