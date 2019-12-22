package com.mycodefu;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

public class Start {
    public static void main(String[] args) throws Throwable{
        Frame frame = new Frame("Lan Test-"+getCurrentIp());
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setSize(320, 240);
//panel.setFocusable(true);
        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setText("Toggle Me");
        panel.add(toggleButton);

        frame.setVisible(true);
        //panel.requestFocusInWindow();
    }
    private static String getCurrentIp() throws Throwable{
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }
    private static void openConnection(){

    }
}
