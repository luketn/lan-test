package com.mycodefu;

import com.mycodefu.websockets.client.WebSocketClientHandler;

public class Start implements LanEventListener, UIEventListener, WebSocketClientHandler.SocketCallback {
    private LanListener lanListener;
    private LanConnector lanConnector;
    private String serverConnectionId;
private UI ui;
    
    public static void main(String[] args) throws Throwable {
        new Start().start();
    }

    private void start() throws Throwable {
        System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Threads for Worker: " + Runtime.getRuntime().availableProcessors() * 2);

        int port = IPUtils.getRandomPort();
        String currentIp = IPUtils.getCurrentIp();

        ui = new UI(currentIp, port, this);
        ui.display();

        lanListener = new LanListener();
        lanListener.listenForMessages(port, this);
    }


    @Override
    public void connectClicked(String remoteIP, int remotePort) {
        lanConnector = new LanConnector();
        lanConnector.connect(remoteIP, remotePort, this);
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
    }

    @Override
    public void clientError(String id, Throwable e) {
        System.out.println(String.format("Error occurred on client %s:\n%s", id, e));
    }

    @Override
    public void connectionReceived(String id) {
        this.serverConnectionId = id;
    }

    @Override
    public void messageReceived(String id, String sourceIpAddress, String message) {
        System.out.println(String.format("Received message from %s: %s", sourceIpAddress, message));
    }
}
