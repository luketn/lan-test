package com.mycodefu;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    private JTextArea messages;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public UI(String IPAddress, int port, UIEventListener uiEventListener) {
        super(String.format("LAN Test - %s:%d", IPAddress, port));

        this.setMinimumSize(new Dimension(640, 480));
        this.setSize(640, 480);
        this.setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(createClientConnectionForm(uiEventListener));
        panel.add(createMessageSendPanel(uiEventListener));
        panel.add(createMessageDisplayPanel());
        this.add(panel);
    }

    private JTextArea createMessageDisplayPanel() {
        messages = new JTextArea();
        messages.getAccessibleContext().setAccessibleName("messages");
        messages.setRows(1);
        messages.setColumns(15);
        messages.setAutoscrolls(true);
        messages.setLineWrap(true);
        messages.setEditable(false);
        return messages;
    }

    private JPanel createMessageSendPanel(UIEventListener uiEventListener) {
        JPanel messagePanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField messageBox = new JTextField();
        messageBox.getAccessibleContext().setAccessibleName("messageBox");
        messageBox.setColumns(50);
        messageBox.setText("Message goes here");
        JLabel messageLabel = new JLabel("Enter Message:", SwingConstants.TRAILING);
        messageLabel.setLabelFor(messageBox);

        messagePanel.add(messageLabel);
        messagePanel.add(messageBox);

        JButton messageButton = new JButton();
        messageButton.setText("Send Message");
        messageButton.addActionListener(e -> uiEventListener.sendMessageClicked(messageBox.getText()));
        JPanel messageButtonSpacer = new JPanel();
        messagePanel.add(messageButtonSpacer);
        messagePanel.add(messageButton);
        return messagePanel;
    }

    private JPanel createClientConnectionForm(UIEventListener uiEventListener) {
        JPanel clientConnectionForm = new JPanel(new GridLayout(3, 2, 10, 10));//new SpringLayout());

        JTextField addressBox = addControlPairToPanel(clientConnectionForm, "Remote Web Socket Address:", "remote web socket address", "wss://echo.websocket.org", 30);
        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> {
            if (connectButton.getText() == "Connect") {
                uiEventListener.connectClicked(addressBox.getText());
                connectButton.setText("Disconnect");
            } else {
                uiEventListener.disconnectClicked();
                connectButton.setText("Connect");
            }
        });
        JPanel connectButtonSpacer = new JPanel();
        clientConnectionForm.add(connectButtonSpacer);
        clientConnectionForm.add(connectButton);
        return clientConnectionForm;
    }

    private JTextField addControlPairToPanel(JPanel panel, String labelText, String accessibleName, String defaultText, int columns) {
        JTextField textField = new JTextField();
        textField.getAccessibleContext().setAccessibleName(accessibleName);
        textField.setColumns(columns);
        textField.setText(defaultText);
        JLabel label = new JLabel(labelText, SwingConstants.TRAILING);
        label.setLabelFor(textField);

        panel.add(label);
        panel.add(textField);

        return textField;
    }

    public void display() {
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
    }

    public JTextArea getMessagesTextArea() {
        return messages;
    }

}
