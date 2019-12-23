package com.mycodefu;

public interface LanEventListener {
    void connectionReceived(String id);
    void messageReceived(String id, String sourceIpAddress, String message);
}
