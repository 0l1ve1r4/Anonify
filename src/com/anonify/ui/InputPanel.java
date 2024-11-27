package com.anonify.ui;

import javax.swing.*;
import java.awt.*;
import com.anonify.utils.Constants;

class InputPanel extends JPanel {
    InputPanel(ChatPanel chatPanel) {
        setLayout(new BorderLayout());
        setBackground(Constants.LIGHTER_GRAY);

        JTextField messageField = new JTextField();
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageField.setBackground(Constants.LIGHTER_GRAY);
        messageField.setForeground(Constants.LIGHT_GRAY);
        messageField.setCaretColor(Constants.LIGHT_GRAY);
        messageField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBackground(Constants.BLUE);
        sendButton.setForeground(Color.WHITE);

        sendButton.addActionListener(_ -> sendMessage(chatPanel, messageField));
        messageField.addActionListener(_ -> sendMessage(chatPanel, messageField));

        add(messageField, BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);
    }

    private void sendMessage(ChatPanel chatPanel, JTextField messageField) {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            chatPanel.addMessage(message, "YOU");
            messageField.setText("");
        }
    }
}
