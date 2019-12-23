package com.mycodefu;

import com.mycodefu.websockets.server.WebSocketServer;
import com.mycodefu.websockets.server.WebSocketServerHandler;
import io.netty.channel.ChannelId;

public class LanListener {
    private WebSocketServerHandler.ServerConnectionCallback callback;
    WebSocketServer webSocketServer;

    public void listenForMessages(WebSocketServerHandler.ServerConnectionCallback callback) {
        this.callback = callback;

        webSocketServer = new WebSocketServer(0, callback);
        webSocketServer.listen();
    }

    public void sendMessage(ChannelId id, String message) {
        webSocketServer.sendMessage(id, message);
    }

    public int getPort() {
        return webSocketServer.getPort();
    }
}
