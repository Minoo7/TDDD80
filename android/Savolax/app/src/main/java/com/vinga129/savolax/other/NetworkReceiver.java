package com.vinga129.savolax.other;

public interface NetworkReceiver {
    <T> void onNetworkReceived(T body);
}
