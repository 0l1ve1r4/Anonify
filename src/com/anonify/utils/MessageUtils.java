package com.anonify.utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MessageUtils {
    public static JPanel createMessagePanel(String message, String sender) {
        // Container panel for alignment
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
        containerPanel.setOpaque(false); // <--- Torna transparente
        containerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Inner message panel
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(sender.equals("YOU") ? Constants.DARK_PURPLE : Constants.PURPLE);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messagePanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE)); // Fix width

        // Message label with wrapping HTML
        JLabel messageLabel = new JLabel("<html><body style='width: 200px;'>" + formatMessage(message) + "</body></html>");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Add message panel to container with alignment
        if (sender.equals("YOU")) {
            containerPanel.add(Box.createHorizontalGlue()); // Push to the right
            containerPanel.add(messagePanel);
        } else {
            containerPanel.add(messagePanel);
            containerPanel.add(Box.createHorizontalGlue()); // Push to the left
        }

        return containerPanel;
    }

    private static String formatMessage(String message) {
        if (message.length() <= Constants.MAX_MESSAGE_STRING) {
            return message;
        }

        StringBuilder formatted = new StringBuilder();
        int index = 0;
        while (index < message.length()) {
            int endIndex = Math.min(index + Constants.MAX_MESSAGE_STRING, message.length());
            formatted.append(message, index, endIndex).append("\n");
            index = endIndex;
        }
        return formatted.toString().trim();
    }
}
