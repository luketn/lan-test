package com.mycodefu;

public interface UIEventListener {
    void connectClicked(String webSocketAddress);
    void disconnectClicked();
    void sendMessageClicked(String text);
}
