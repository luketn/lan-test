package com.mycodefu;

public class Start implements MessageListener, UIEventListener {
    private LanListener lanListener;
    private LanConnector lanConnector;

    public static void main(String[] args) throws Throwable {
        new Start().start();
    }

    private void start() throws Throwable {
        System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Threads for Worker: " + Runtime.getRuntime().availableProcessors() * 2);

        int port = IPUtils.getRandomPort();
        String currentIp = IPUtils.getCurrentIp();

        UI ui = new UI(currentIp, port, this);
        ui.display();

        lanListener = new LanListener();
        lanListener.listenForMessages(port, new Start());
    }

    @Override
    public void messageReceived(String sourceIpAddress, String message) {
        System.out.println(String.format("Received message from %s: %s", sourceIpAddress, message));
    }

    @Override
    public void connectClicked(String remoteIP, int remotePort) {
        lanConnector = new LanConnector();
        lanConnector.connect(remoteIP, remotePort);
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
        }
    }
}
