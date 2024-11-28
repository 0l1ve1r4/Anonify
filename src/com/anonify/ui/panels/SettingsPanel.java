package com.anonify.ui.panels;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.ui.components.TabManager;
import com.anonify.utils.Constants;

public class SettingsPanel extends JPanel {
    public SettingsPanel(TabManager tabManager, Services torService) {
        // Set the layout and background color for the panel
        setLayout(new GridLayout(5, 1, 1, 0)); // Added spacing between components
        setBackground(Constants.LIGHTER_GRAY);

        // Create buttons with consistent styling
        JButton configureOnionButton = createStyledButton("Configure Onion Server");
        configureOnionButton.addActionListener(e -> tabManager.openConfigureOnionTab());

        JButton connectOnionButton = createStyledButton("Connect to an Onion Server");
        connectOnionButton.addActionListener(e -> tabManager.openConnectOnionTab());

        // Add buttons to the panel
        add(configureOnionButton);
        add(connectOnionButton);
    }

    // Method to create a button with consistent styling
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(Constants.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Constants.LIGHT_GRAY, 2));

        // Change button background color on hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.DARK_GRAY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Constants.DARK_GRAY);
            }
        });

        // Align the button in the center of the panel
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}
