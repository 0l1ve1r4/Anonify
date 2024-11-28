package com.anonify.ui.panels;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.ui.components.TabManager;
import com.anonify.utils.Constants;

public class SettingsPanel extends JPanel {
    public SettingsPanel(TabManager tabManager, Services torService) {
        setLayout(new GridLayout(20, 1));
        setBackground(Constants.LIGHTER_GRAY);

        JButton configureOnionButton = new JButton("Configure Onion Server");
        configureOnionButton.addActionListener(e -> tabManager.openConfigureOnionTab());

        JButton connectOnionButton = new JButton("Connect to an Onion Server");
        connectOnionButton.addActionListener(e -> tabManager.openConnectOnionTab());

        add(configureOnionButton);
        add(connectOnionButton);
    }
}
