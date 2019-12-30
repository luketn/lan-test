package com.mycodefu;

import com.mycodefu.websockets.client.WebSocketClientHandler;
import com.mycodefu.websockets.server.WebSocketServerHandler;
import io.netty.channel.ChannelId;

public class LanTest implements WebSocketServerHandler.ServerConnectionCallback, UIEventListener, WebSocketClientHandler.SocketCallback {
    private LanListener lanListener;
    private LanConnector lanConnector;
    private ChannelId serverConnectionId;
    private UI ui;

    public static void main(String[] args) throws Throwable {
        new LanTest().start();
    }

    private void start() throws Throwable {
        System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Threads for Worker: " + Runtime.getRuntime().availableProcessors() * 2);

        lanListener = new LanListener();
        lanListener.listenForMessages(this);

        int port = lanListener.getPort();
        String currentIp = IPUtils.getCurrentIp();

        ui = new UI(currentIp, port, this);
        ui.display();

    }

    @Override
    public void connectClicked(String webSocketAddress) {
        lanConnector = new LanConnector();
        lanConnector.connect(webSocketAddress, this);
    }

    @Override
    public void disconnectClicked() {
        if (lanConnector != null) {
            lanConnector.disconnect();
            lanConnector = null;
        }
    }

    @Override
    public void sendMessageClicked(String text) {
        if (lanConnector != null) {
            lanConnector.sendMessage(text);
        } else if (serverConnectionId != null) {
            lanListener.sendMessage(serverConnectionId, text);
        }
    }

    @Override
    public void clientDisconnected(String id) {
        System.out.println(String.format("Client %s disconnected.", id));
    }

    @Override
    public void clientConnected(String id) {
        System.out.println(String.format("Client %s connected.", id));
    }

    @Override
    public void clientMessageReceived(String id, String text) {
        System.out.println(String.format("Message received from client %s: %s", id, text));
        ui.getMessagesTextArea().setText(String.format("%s\n%s", text, ui.getMessagesTextArea().getText()));
    }

    @Override
    public void clientError(String id, Throwable e) {
        System.out.println(String.format("Error occurred on client %s:\n%s", id, e));
    }

    @Override
    public void clientConnected(ChannelId id) {
        this.serverConnectionId = id;
    }

    @Override
    public void messageReceived(ChannelId id, String sourceIpAddress, String message) {
        System.out.println(String.format("Received message from %s: %s", sourceIpAddress, message));

        ui.getMessagesTextArea().setText(String.format("%s\n%s", message, ui.getMessagesTextArea().getText()));
    }

    @Override
    public void clientDisconnected(ChannelId id) {
        System.out.println("Disconnected");
    }
}
