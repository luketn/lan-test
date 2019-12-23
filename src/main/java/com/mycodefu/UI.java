package com.mycodefu;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    public UI(String IPAddress, int port, UIEventListener uiEventListener) {
        super(String.format("LAN Test - %s:%d", IPAddress, port));
        this.setSize(640, 480);

        int numPairs = 5;

        JPanel inputs = new JPanel(new SpringLayout());
        this.add(inputs);

        JTextField ipBox = new JTextField();
        ipBox.getAccessibleContext().setAccessibleName("remote ip");
        ipBox.setColumns(15);
        ipBox.setText("192.168.0.78");
        JLabel ipLabel = new JLabel("Remote IP Address:", SwingConstants.TRAILING);
        ipLabel.setLabelFor(ipBox);
        inputs.add(ipLabel);
        inputs.add(ipBox);

        JTextField portBox = new JTextField();
        portBox.getAccessibleContext().setAccessibleName("remote port");
        portBox.setColumns(5);
        portBox.setText("50000");
        JLabel portLabel = new JLabel("Remote Port Number:", SwingConstants.TRAILING);
        portLabel.setLabelFor(portBox);
        inputs.add(portLabel);
        inputs.add(portBox);

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> {
            if (connectButton.getText()=="Connect"){
                uiEventListener.connectClicked(ipBox.getText(), Integer.parseInt(portBox.getText()));
                connectButton.setText("Disconnect");
            } else {
                uiEventListener.disconnectClicked();
                connectButton.setText("Connect");
            }
        });
        JPanel connectButtonSpacer = new JPanel();
        inputs.add(connectButtonSpacer);
        inputs.add(connectButton);

        JTextField messageBox = new JTextField();
        messageBox.getAccessibleContext().setAccessibleName("messageBox");
        messageBox.setColumns(50);
        messageBox.setText("Message Goes Here");
        JLabel messageLabel = new JLabel("Enter Message:", SwingConstants.TRAILING);
        messageLabel.setLabelFor(messageBox);
        inputs.add(messageLabel);
        inputs.add(messageBox);

        JButton messageButton = new JButton();
        messageButton.setText("Send Message");
        messageButton.addActionListener(e -> uiEventListener.sendMessageClicked(messageBox.getText()));
        JPanel messageButtonSpacer = new JPanel();
        inputs.add(messageButtonSpacer);
        inputs.add(messageButton);

        //Lay out the panel.
        makeCompactGrid(inputs,
                numPairs, 2, //rows, cols
                20, 20,        //initX, initY
                20, 20);       //xPad, yPad

        this.pack();

    }

    public void display() {
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
    }

    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).
                                getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).
                                getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
            int row, int col,
            Container parent,
            int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }
}
