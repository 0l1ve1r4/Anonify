package com.anonify.ui.components;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.utils.Constants;

public class LogoPanel extends JPanel {
    public LogoPanel(ChatPanel chatPanel, Services torService, TabManager tabManager) {
        setBackground(Constants.DARK_GRAY);
        setPreferredSize(new Dimension(0, 50));
        setLayout(new BorderLayout());

        JLabel logoLabel = createLogoLabel();
        JPanel centeredLogoPanel = createCenteredPanel(logoLabel);
        add(centeredLogoPanel, BorderLayout.CENTER);

        JPanel iconsPanel = createIconsPanel(chatPanel, tabManager);
        add(iconsPanel, BorderLayout.EAST);
    }

    private JLabel createLogoLabel() {
        ImageIcon logoIcon = new ImageIcon("src/res/logo-no-bg-centralized.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledLogo), SwingConstants.CENTER);
    }

    private JPanel createCenteredPanel(JLabel logoLabel) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Constants.DARK_GRAY);
        panel.add(logoLabel);
        return panel;
    }

    private JPanel createIconsPanel(ChatPanel chatPanel, TabManager tabManager) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setBackground(Constants.DARK_GRAY);

        JButton settingsButton = createIconButton("src/res/settings-icon-white.png", "Settings");
        JButton helpButton = createIconButton("src/res/help-icon-white.png", "Help");

        settingsButton.addActionListener(e -> tabManager.openSettingsTab());
        helpButton.addActionListener(e -> chatPanel.showHelp());

        panel.add(settingsButton);
        panel.add(helpButton);
        return panel;
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
