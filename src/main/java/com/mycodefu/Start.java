package com.mycodefu;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.Random;

public class Start implements MessageListener {
    private static LanListener lanListener;

    public static void main(String[] args) throws Throwable {
        int port = getRandomPort();
        Frame frame = new Frame("Lan Test-" + getCurrentIp()+":"+port);
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setSize(320, 240);
        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setText("Toggle Me");
        panel.add(toggleButton);

        frame.setVisible(true);

        lanListener = new LanListener();
        lanListener.listenForMessages(new Start());
    }

    private static String getCurrentIp() throws Throwable {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }

    private static int getRandomPort()  {
        Random rand = new Random();
        return rand.nextInt(15000)+50000;
    }

    @Override
    public void messageReceived(String sourceIpAddress, String message) {
        System.out.println(String.format("Received message from %s: %s", sourceIpAddress, message));
    }
}
