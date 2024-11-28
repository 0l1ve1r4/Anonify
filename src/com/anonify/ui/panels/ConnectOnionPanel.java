package com.anonify.ui.panels;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.ui.components.ChatPanel;
import com.anonify.utils.Constants;

public class ConnectOnionPanel extends JPanel {
    public ConnectOnionPanel(Services torService, ChatPanel chatPanel) {
        setLayout(new GridLayout(20, 1));
        setBackground(Constants.LIGHTER_GRAY);

        JLabel onionAddressLabel = new JLabel("Onion Address:");
        onionAddressLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField onionAddressField = new JTextField();

        JLabel proxyHostLabel = new JLabel("Tor Proxy Host:");
        proxyHostLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField proxyHostField = new JTextField("127.0.0.1");

        JLabel proxyPortLabel = new JLabel("Tor Proxy Port:");
        proxyPortLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField proxyPortField = new JTextField("9050");

        JLabel serverPortLabel = new JLabel("Server Port:");
        serverPortLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField serverPortField = new JTextField("12345");

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> connectToServer(torService, chatPanel, onionAddressField, proxyHostField, proxyPortField, serverPortField));

        add(onionAddressLabel);
        add(onionAddressField);
        add(proxyHostLabel);
        add(proxyHostField);
        add(proxyPortLabel);
        add(proxyPortField);
        add(serverPortLabel);
        add(serverPortField);
        add(connectButton);
    }

    private void connectToServer(Services torService, ChatPanel chatPanel, JTextField onionAddressField, JTextField proxyHostField, JTextField proxyPortField, JTextField serverPortField) {
        String onionAddress = onionAddressField.getText();
        String proxyHost = proxyHostField.getText();
        String proxyPort = proxyPortField.getText();
        String serverPort = serverPortField.getText();

        new Thread(() -> {
            try {
                torService.connectToOnionServer(onionAddress, proxyHost, proxyPort, serverPort, chatPanel);
                SwingUtilities.invokeLater(() -> chatPanel.addMessage("Connected to server!", "BOT"));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> chatPanel.addMessage("Connection failed: " + ex.getMessage(), "BOT"));
            }
        }).start();
    }
}
