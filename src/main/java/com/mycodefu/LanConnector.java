package com.mycodefu;

import com.mycodefu.com.mycodefu.websockets.client.WebSocketClient;
import com.mycodefu.com.mycodefu.websockets.client.WebSocketClientHandler;

public class LanConnector {
    private WebSocketClient webSocketClient;

    public void connect(String ipAddress, int port, WebSocketClientHandler.SocketCallback callback) {
        System.out.println(String.format("Connecting to %s:%d!", ipAddress, port));
        webSocketClient = new WebSocketClient(String.format("ws://%s:%d/", ipAddress, port), callback);
    }
    public void disconnect() {
        System.out.println("Disconnecting!");
        webSocketClient.disconnect();
    }
    public void sendMessage(String message) {
        System.out.println(String.format("Sending message '%s'!", message));
        webSocketClient.sendMessage(message);
    }

}
