package com.anonify.ui;

import javax.swing.*;
import java.awt.*;
import com.anonify.services.Services;
import com.anonify.ui.components.ChatPanel;
import com.anonify.ui.components.InputPanel;
import com.anonify.ui.components.LogoPanel;
import com.anonify.ui.components.TabManager;

class MainFrame extends JFrame {
    MainFrame(String title, int width, int height, Services torService, ChatPanel chatPanel) {
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setLayout(new BorderLayout());

        TabManager tabManager = new TabManager(torService, chatPanel);
        add(new LogoPanel(chatPanel, torService, tabManager), BorderLayout.NORTH);
        add(tabManager.getTabbedPane(), BorderLayout.CENTER);
        add(new InputPanel(chatPanel, torService), BorderLayout.SOUTH);

        setVisible(true);
    }
}
