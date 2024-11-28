package com.anonify.ui;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.utils.Constants;

class MainFrame extends JFrame {
    private Services torService;
    private ChatPanel chatPanel;
    private JTabbedPane tabbedPane;

    MainFrame(String title, int width, int height, Services torService, ChatPanel chatPanel) {        
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setLayout(new BorderLayout());

        this.torService = torService;
        this.chatPanel = chatPanel;

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Constants.DARK_GRAY);
        tabbedPane.setForeground(Constants.LIGHT_GRAY);

        

        // Main Chat Panel
        tabbedPane.addTab("Chat", chatPanel.getScrollPane());

        add(createLogoPanel(chatPanel), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(new InputPanel(chatPanel, torService), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLogoPanel(ChatPanel panel) {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Constants.DARK_GRAY);
        logoPanel.setPreferredSize(new Dimension(getWidth(), 50));
        logoPanel.setLayout(new BorderLayout());
    
        ImageIcon logoIcon = new ImageIcon("src/res/logo-no-bg-centralized.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH); 
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo), SwingConstants.CENTER);
        
        JPanel centeredLogoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centeredLogoPanel.setBackground(Constants.DARK_GRAY);
        centeredLogoPanel.add(logoLabel);
    
        logoPanel.add(centeredLogoPanel, BorderLayout.CENTER);
    
        JPanel iconsPanel = new JPanel();
        iconsPanel.setBackground(Constants.DARK_GRAY);
        iconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        JButton settingsButton = createIconButton("src/res/settings-icon-white.png", "Settings");
        JButton helpButton = createIconButton("src/res/help-icon-white.png", "Help");
        
        settingsButton.addActionListener(_ -> openSettingsTab());
        helpButton.addActionListener(_ -> panel.showHelp());
        
        iconsPanel.add(settingsButton);
        iconsPanel.add(helpButton);

        logoPanel.add(iconsPanel, BorderLayout.EAST);
        return logoPanel;
    }

    private void openSettingsTab() {
        JPanel settingsPanel = new JPanel(new GridLayout(20, 1));
        settingsPanel.setBackground(Constants.LIGHTER_GRAY);

        JButton configureOnionButton = new JButton("Configure Onion Server");
        configureOnionButton.addActionListener(_ -> openConfigureOnionTab());

        JButton connectOnionButton = new JButton("Connect to an Onion Server");
        connectOnionButton.addActionListener(_ -> openConnectOnionTab());

        settingsPanel.add(configureOnionButton);
        settingsPanel.add(connectOnionButton);

        addOrSwitchTab("Settings", settingsPanel);
    }

    private void openConfigureOnionTab() {
        JPanel configurePanel = new JPanel(new GridLayout(20, 1));
        configurePanel.setBackground(Constants.LIGHTER_GRAY);

        JLabel portLabel = new JLabel("Port:");
        portLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField portField = new JTextField();
        portField.setText("12345");


        JLabel hiddenServiceLabel = new JLabel("Hidden Service Hostname File:");
        hiddenServiceLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField hiddenServiceField = new JTextField();
        hiddenServiceField.setText("/var/lib/tor/hidden_service/hostname");

        JButton startButton = new JButton("Start Server");
        startButton.addActionListener(_ -> {
            String hostFilePath = hiddenServiceField.getText();
            String portStr = portField.getText();

            new Thread(() -> {
                try {
                    torService.startOnionServer(hostFilePath, portStr, chatPanel);
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> 
                        chatPanel.addMessage("Failed to start server: " + ex.getMessage(), "BOT")
                    );
                }
            }).start();
            
        });

        configurePanel.add(portLabel);
        configurePanel.add(portField);
        configurePanel.add(hiddenServiceLabel);
        configurePanel.add(hiddenServiceField);
        configurePanel.add(startButton);

        addOrSwitchTab("Configure Onion Server", configurePanel);
    }

    private void openConnectOnionTab() {
        JPanel connectPanel = new JPanel(new GridLayout(20, 1));
        connectPanel.setBackground(Constants.LIGHTER_GRAY);

        JLabel onionAddressLabel = new JLabel("Onion Address:");
        onionAddressLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField onionAddressField = new JTextField();
        onionAddressField.setForeground(Constants.DARK_GRAY);

        JLabel proxyHostLabel = new JLabel("Tor Proxy Host:");
        proxyHostLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField proxyHostField = new JTextField();
        proxyHostField.setText("127.0.0.1");
        
        JLabel proxyPortLabel = new JLabel("Tor Proxy Port:");
        proxyPortLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField proxyPortField = new JTextField();
        proxyPortField.setText("9050");


        JLabel serverPortLabel = new JLabel("Server Port:");
        serverPortLabel.setForeground(Constants.LIGHT_GRAY);
        JTextField serverPortField = new JTextField();
        serverPortField.setText("12345");

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(_ -> {
            String onionAddress = onionAddressField.getText();
            String proxyHost = proxyHostField.getText();
            String proxyPort = proxyPortField.getText();
            String serverPort = serverPortField.getText();

            new Thread(() -> {
                try {
                    torService.connectToOnionServer(onionAddress, proxyHost, proxyPort, serverPort, chatPanel); 
                    SwingUtilities.invokeLater(() -> 
                        chatPanel.addMessage("Connected to server!", "BOT")
                    );
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> 
                        chatPanel.addMessage("Connection failed: " + ex.getMessage(), "BOT")
                    );
                }
            }).start();
        });

        connectPanel.add(onionAddressLabel);
        connectPanel.add(onionAddressField);
        connectPanel.add(proxyHostLabel);
        connectPanel.add(proxyHostField);
        connectPanel.add(proxyPortLabel);
        connectPanel.add(proxyPortField);
        connectPanel.add(serverPortLabel);
        connectPanel.add(serverPortField);
        connectPanel.add(connectButton);

        addOrSwitchTab("Connect to Onion Server", connectPanel);
    }

    private void addOrSwitchTab(String title, JPanel panel) {
        int index = tabbedPane.indexOfTab(title);
        if (index >= 0) {
            tabbedPane.setSelectedIndex(index);
        } else {
            tabbedPane.addTab(title, panel);
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }
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
