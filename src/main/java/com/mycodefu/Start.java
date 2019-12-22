package com.mycodefu;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

public class Start {
    public static void main(String[] args) throws Throwable{
        Frame frame = new Frame("Lan Test-"+getCurrentIp());
        frame.setSize(320, 240);

        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setText("Toggle Me");
        frame.add(toggleButton);

        frame.setVisible(true);
    }
    private static String getCurrentIp() throws Throwable{
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }
    private static void openConnection(){

    }
}
