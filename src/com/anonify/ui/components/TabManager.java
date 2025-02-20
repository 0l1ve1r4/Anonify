package com.anonify.ui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
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

    void addOrSwitchTab(String title, JPanel panel, Icon icon) {
        int index = tabbedPane.indexOfTab(title);
        if (index >= 0) {
            tabbedPane.setSelectedIndex(index);
        } else {
            tabbedPane.addTab(title, icon, panel);
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }
    }

    private Icon getIconFromPath(String path) {
        ImageIcon icon = new ImageIcon(path);
 
        return icon;
    }
    
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setUI(new CustomTabbedPaneUI());
        tabs.setBackground(Constants.DARK_GRAY);
        tabs.setForeground(Constants.LIGHT_GRAY);
    
        tabs.addTab("Chat", getIconFromPath("src/res/icons/16x16/relay.png"), chatPanel.getScrollPane());

        return tabs;
    }
    
    public void openSettingsTab() {
        addOrSwitchTab("Settings", new SettingsPanel(this, torService), 
        getIconFromPath("src/res/icons/16x16/onion-alt.png"));
    }
    
    public void openConfigureOnionTab() {
        addOrSwitchTab("Configure Onion Server", new ConfigureOnionPanel(torService, chatPanel), 
        getIconFromPath("src/res/icons/16x16/running.png"));
    }
    
    public void openConnectOnionTab() {
        addOrSwitchTab("Connect to Onion Server", new ConnectOnionPanel(torService, chatPanel), 
        getIconFromPath("src/res/icons/16x16/exit.png"));
    }
    
    private static class CustomTabbedPaneUI extends BasicTabbedPaneUI {
        private final Color selectedTabColor = Constants.LIGHTER_GRAY;
        private final Color tabLineColor = Constants.DARK_GRAY;
    
        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            g.setColor(isSelected ? selectedTabColor : tabPane.getBackground());
            g.fillRect(x, y, w, h);
        }
    
        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            g.setColor(tabLineColor); 
            int tabHeight = tabPane.getBounds().height;
            int lineYPosition = tabHeight - 2; 
            
            g.drawLine(0, lineYPosition, tabPane.getWidth(), lineYPosition);
        }
    }
    
}
