package com.anonify.ui.panels;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.ui.components.ChatPanel;
import com.anonify.utils.Constants;

public class ConfigureOnionPanel extends JPanel {
    public ConfigureOnionPanel(Services torService, ChatPanel chatPanel) {
        setLayout(new GridLayout(20, 1));
        setBackground(Constants.LIGHTER_GRAY);

        JLabel portLabel = new JLabel("Port:");
        portLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField portField = new JTextField("12345");

        JLabel hiddenServiceLabel = new JLabel("Hidden Service Hostname File:");
        hiddenServiceLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField hiddenServiceField = new JTextField("/var/lib/tor/hidden_service/hostname");

        JButton startButton = new JButton("Start Server");
        startButton.addActionListener(e -> startOnionServer(torService, chatPanel, portField, hiddenServiceField));

        add(portLabel);
        add(portField);
        add(hiddenServiceLabel);
        add(hiddenServiceField);
        add(startButton);
    }

    private void startOnionServer(Services torService, ChatPanel chatPanel, JTextField portField, JTextField hiddenServiceField) {
        String port = portField.getText();
        String hiddenServicePath = hiddenServiceField.getText();

        new Thread(() -> {
            try {
                torService.startOnionServer(hiddenServicePath, port, chatPanel);
                SwingUtilities.invokeLater(() -> chatPanel.addMessage("Onion server started successfully!", "BOT"));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> chatPanel.addMessage("Failed to start server: " + ex.getMessage(), "BOT"));
            }
        }).start();
    }
}
