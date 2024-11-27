package com.anonify.main;
import com.anonify.ui.UI;
import com.anonify.ui.ChatPanel;

public class Main {
    public static void main(String[] args) {
        ChatPanel chatPanel = new ChatPanel();
        UI userInterface = new UI(chatPanel);
        userInterface.startUI("Anonify", 980, 800);
    }
}
