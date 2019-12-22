package com.mycodefu;

public interface MessageListener {
    void messageReceived(String sourceIpAddress, String message);
}
