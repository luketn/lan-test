package com.mycodefu;

public class Start implements MessageListener, UIEventListener {
    private static LanListener lanListener;

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
        System.out.println(String.format("Connecting to %s:%d!", remoteIP, remotePort));
    }

    @Override
    public void disconnectClicked() {
        System.out.println("Disconnecting!");
    }

    @Override
    public void sendMessage(String text) {
        System.out.println(String.format("Sending message '%s'!", text));
    }
}
