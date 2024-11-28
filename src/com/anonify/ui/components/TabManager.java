package com.anonify.ui.components;

import javax.swing.*;
import com.anonify.services.Services;
import com.anonify.ui.panels.ConnectOnionPanel;
import com.anonify.ui.panels.ConfigureOnionPanel;
import com.anonify.ui.panels.SettingsPanel;
import com.anonify.utils.Constants;

public class TabManager {
    private final JTabbedPane tabbedPane;
    private final Services torService;
    private final ChatPanel chatPanel;

    public TabManager(Services torService, ChatPanel chatPanel) {
        this.torService = torService;
        this.chatPanel = chatPanel;
        this.tabbedPane = createTabbedPane();
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    void addOrSwitchTab(String title, JPanel panel) {
        int index = tabbedPane.indexOfTab(title);
        if (index >= 0) {
            tabbedPane.setSelectedIndex(index);
        } else {
            tabbedPane.addTab(title, panel);
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Constants.DARK_GRAY);
        tabs.setForeground(Constants.LIGHT_GRAY);
        tabs.addTab("Chat", chatPanel.getScrollPane());
        return tabs;
    }

    public void openSettingsTab() {
        addOrSwitchTab("Settings", new SettingsPanel(this, torService));
    }

    public void openConfigureOnionTab() {
        addOrSwitchTab("Configure Onion Server", new ConfigureOnionPanel(torService, chatPanel));
    }

    public void openConnectOnionTab() {
        addOrSwitchTab("Connect to Onion Server", new ConnectOnionPanel(torService, chatPanel));
    }
}
