package com.mycodefu;

public class LanConnector {
    public boolean connect(String ipAddress, int port) {
        System.out.println(String.format("Connecting to %s:%d!", ipAddress, port));
        return false;
    }
    public void disconnect() {
        System.out.println("Disconnecting!");
    }
    public void sendMessage(String message) {
        System.out.println(String.format("Sending message '%s'!", message));
    }
}
