package com.vinga129.a3;

public interface NetworkReceiver {
    <T> void onNetworkReceived(T body);
}
