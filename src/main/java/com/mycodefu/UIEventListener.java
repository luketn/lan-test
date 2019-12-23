package com.mycodefu;

public interface UIEventListener {
    void connectClicked(String remoteIP, int remotePort);
    void disconnectClicked();
    void toggleClicked();
}
