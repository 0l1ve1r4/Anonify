package com.anonify.ui.panels;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.ui.components.ChatPanel;
import com.anonify.utils.Constants;
import java.awt.datatransfer.*;
import java.awt.event.*;

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
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                    chatPanel.addMessage("Failed to start server: " + ex.getMessage(), "BOT")
                );
            }
        }).start();
    
        SwingUtilities.invokeLater(() -> {
            // Dispose of all open windows before showing the server status window
            for (Window window : Window.getWindows()) {
                window.dispose();
            }
            ServerStatusWindow statusWindow = new ServerStatusWindow(torService.getOnionAddress());
            statusWindow.setVisible(true);
        });
    
    }
}

class ServerStatusWindow extends JFrame {
    public ServerStatusWindow(String onionAddress) {
        setTitle("Server Status");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Apply Nimbus look-and-feel for a modern UI
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Ignore if Nimbus is not available
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Constants.PURPLE);

        JLabel runningLabel = new JLabel("Server Running");
        runningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        runningLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        runningLabel.setForeground(Constants.DARK_PURPLE);

        JLabel infoLabel = new JLabel("A free space is opened!");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLabel.setForeground(Constants.DARK_PURPLE);

        // Panel to hold the onion address and copy icon
        JPanel addressPanel = new JPanel(new BorderLayout(5, 0));
        addressPanel.setBackground(Constants.PURPLE);

        // Load clipboard icon (assuming it's available in resources)
        ImageIcon icon = new ImageIcon("src/res/icons/16x16/fingerprint.png");

        // Scale the image 3x
        Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        
        // Create a new ImageIcon with the resized image
        ImageIcon scaledIcon = new ImageIcon(image);
        
        JLabel copyLabel = new JLabel(scaledIcon);
        copyLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copyLabel.setToolTipText("Copy Onion Address");

        // Copy functionality
        copyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StringSelection selection = new StringSelection(onionAddress);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                JOptionPane.showMessageDialog(ServerStatusWindow.this, 
                    "Onion address copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        addressPanel.add(copyLabel, BorderLayout.CENTER);

        // Organize components vertically
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(runningLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(infoLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(addressPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        add(mainPanel, BorderLayout.CENTER);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerStatusWindow window = new ServerStatusWindow("exampleonionaddress.onion");
            window.setVisible(true);
        });
    }
}