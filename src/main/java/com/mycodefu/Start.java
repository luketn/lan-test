package com.mycodefu;

import javax.swing.*;
import java.awt.*;

public class Start {
    public static void main(String[] args) {
        Frame frame = new Frame("Lan Test");
        frame.setSize(320, 240);

        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setText("Toggle Me");
        frame.add(toggleButton);

        frame.setVisible(true);
    }
    private static void openConnection(){

    }
}
