package com.mycodefu;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.Random;

public class Start implements MessageListener {
    private static LanListener lanListener;

    public static void main(String[] args) throws Throwable {
        System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Threads for Worker: " + Runtime.getRuntime().availableProcessors() * 2);

        int port = getRandomPort();
        JFrame frame = new JFrame("Lan Test-" + getCurrentIp()+":"+port);
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setSize(320, 240);
        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setText("Toggle Me");
        panel.add(toggleButton);

        JTextField ipBox = new JTextField();
        ipBox.getAccessibleContext().setAccessibleName("ipBox");
                ipBox.setColumns(15);
                panel.add(ipBox);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);

        lanListener = new LanListener();
        lanListener.listenForMessages(port, new Start());
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
