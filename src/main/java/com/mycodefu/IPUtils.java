package com.mycodefu;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

public class IPUtils {
    static String getCurrentIp() throws Throwable {
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

    static int getRandomPort()  {
        Random rand = new Random();
        return rand.nextInt(15000)+50000;
    }
}
