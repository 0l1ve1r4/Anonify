package com.anonify.ui;

import javax.swing.SwingUtilities;

import com.anonify.services.Services;
import com.anonify.ui.components.ChatPanel;

public class UI {
    private final Services services;
    private final ChatPanel chatPanel;

    public UI(Services services) {
        this.services   = services;
        this.chatPanel  = new ChatPanel(); 
    };

    public void startUI(String title, int width, int height){
        SwingUtilities.invokeLater(() -> new MainFrame(title, width, height, services, chatPanel));
    }

    public void sendMessage(String message, String sender){
        SwingUtilities.invokeLater(() -> {
            chatPanel.addMessage(message, sender);
        });
    }

}
