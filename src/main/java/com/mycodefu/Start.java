package com.mycodefu;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
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

        System.out.println("\nList of network interfaces:");
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            displayInterfaceInformation(netint);

        return inetAddress.getHostAddress();
    }

    static void displayInterfaceInformation(NetworkInterface netint) {
        System.out.printf("Display name: %s\n", netint.getDisplayName());
        System.out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("InetAddress: %s\n", inetAddress);
        }
        System.out.printf("\n");
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
