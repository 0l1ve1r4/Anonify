package com.anonify.ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import com.anonify.services.TorService;

class MainFrame extends JFrame {
    private TorService torService;

    MainFrame(String title, int width, int height, ChatPanel chatPanel) {
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setLayout(new BorderLayout());

        torService = new TorService(chatPanel);

        add(createLogoPanel(chatPanel), BorderLayout.NORTH);
        add(chatPanel.getScrollPane(), BorderLayout.CENTER);
        add(new InputPanel(chatPanel), BorderLayout.SOUTH);

        setVisible(true);
    }



    private JPanel createLogoPanel(ChatPanel panel) {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.DARK_GRAY);
        logoPanel.setPreferredSize(new Dimension(getWidth(), 50));
        logoPanel.setLayout(new BorderLayout());
    
        ImageIcon logoIcon = new ImageIcon("src/res/logo-no-bg-centralized.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH); // Scale to desired size
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo), SwingConstants.CENTER);
        
        JPanel centeredLogoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centeredLogoPanel.setBackground(Color.DARK_GRAY);
        centeredLogoPanel.add(logoLabel);
    
        logoPanel.add(centeredLogoPanel, BorderLayout.CENTER);
    
        JPanel iconsPanel = new JPanel();
        iconsPanel.setBackground(Color.DARK_GRAY);
        iconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        JButton settingsButton = createIconButton("src/res/settings-icon-white.png", "Settings");
        JButton helpButton = createIconButton("src/res/help-icon-white.png", "Settings");
        
        settingsButton.addActionListener(_ -> openSettingsWindow(panel));
        helpButton.addActionListener(_ -> panel.showHelp());
        
        iconsPanel.add(settingsButton);
        iconsPanel.add(helpButton);

        logoPanel.add(iconsPanel, BorderLayout.EAST);
        return logoPanel;
    }
    

    private void openSettingsWindow(ChatPanel panel) {
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new GridLayout(3, 1));
    
        JButton configureOnionButton = new JButton("Configure Onion Server");
        configureOnionButton.addActionListener(_ -> {
            try {
                String onionAddress = torService.getOnionAddress();
                panel.addMessage("Server Onion Address:\n" + onionAddress, "BOT");
    
                new Thread(() -> {
                    try {
                        torService.startChatServer();
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                            panel.addMessage("Failed to start the server: " + ex.getMessage(), "BOT")
                        );
                    }
                }).start();
            } catch (IOException ex) {
                panel.addMessage("Failed to retrieve Onion Address: " + ex.getMessage(), "BOT");
            }
        });
    
        JButton connectOnionButton = new JButton("Connect to an Onion Server");
        connectOnionButton.addActionListener(_ -> {
            String onionAddress = JOptionPane.showInputDialog(
                settingsFrame,
                "Enter Onion Server Address:",
                "Connect to Onion Server",
                JOptionPane.PLAIN_MESSAGE
            );
    
            if (onionAddress != null && !onionAddress.trim().isEmpty()) {
                new Thread(() -> {
                    try {
                        torService.connectToServer(onionAddress);
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                            panel.addMessage("Failed to connect to the server: " + ex.getMessage(), "BOT")
                        );
                    }
                }).start();
            } else if (onionAddress != null) { // User clicked OK but entered no address
                panel.addMessage("Onion Address cannot be empty.", "BOT");
            }
        });
    
        settingsFrame.add(configureOnionButton);
        settingsFrame.add(connectOnionButton);
        settingsFrame.setVisible(true);
    }
    

    private JButton createIconButton(String iconPath, String tooltip) {
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        button.setToolTipText(tooltip);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

}
