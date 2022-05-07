package com.vinga129.savolax;

public interface NetworkReceiver {
    <T> void onNetworkReceived(T body);
}
