package com.mycodefu;

import com.mycodefu.websockets.client.WebSocketClient;
import com.mycodefu.websockets.client.WebSocketClientHandler;

public class LanConnector {
    private WebSocketClient webSocketClient;

    public void connect(String webSocketAddress, WebSocketClientHandler.SocketCallback callback) {
        System.out.println(String.format("Connecting to %s!", webSocketAddress));
        webSocketClient = new WebSocketClient(webSocketAddress, callback);
        webSocketClient.connect();
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
