package com.anonify.ui;

import javax.swing.SwingUtilities;

public class UI {
    private final ChatPanel chatPanel;

    public UI(ChatPanel panel) {
        this.chatPanel = panel;
    };

    public void startUI(String title, int width, int height){
        SwingUtilities.invokeLater(() -> new MainFrame(title, width, height, chatPanel));
    }

    public void sendMessage(String message, String sender){
        SwingUtilities.invokeLater(() -> {
            chatPanel.addMessage(message, sender);
        });
    }

}
