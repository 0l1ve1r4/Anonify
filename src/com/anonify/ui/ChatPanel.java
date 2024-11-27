package com.anonify.ui;

import java.awt.*;
import javax.swing.*;
import com.anonify.utils.Constants;
import com.anonify.utils.MessageUtils;

public class ChatPanel {
    private final JPanel chatPanel;
    private final JScrollPane scrollPane;
    private int messageIndex = 0;

    public ChatPanel() {
        chatPanel = new JPanel(new GridBagLayout());
        chatPanel.setBackground(Constants.DARK_GRAY);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setBackground(Constants.LIGHTER_GRAY);
        verticalBar.setUI(new CustomScrollBarUI());

        scrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustMessageWidths();
                SwingUtilities.invokeLater(() -> scrollToBottom());
            }
        });

        SwingUtilities.invokeLater(() -> scrollToBottom());
    }

    JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addMessage(String message, String sender) {
        JPanel messageContainer = MessageUtils.createMessagePanel(message, sender);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = messageIndex++;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        chatPanel.add(messageContainer, gbc);

        chatPanel.revalidate();
        chatPanel.repaint();

        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> scrollToBottom()));
    }

    void adjustMessageWidths() {
        int newWidth = scrollPane.getViewport().getWidth() - 20;
        for (Component component : chatPanel.getComponents()) {
            if (component instanceof JPanel panel) {
                panel.setPreferredSize(new Dimension(newWidth, panel.getPreferredSize().height));
                panel.setMaximumSize(new Dimension(newWidth, Integer.MAX_VALUE));
                panel.setMinimumSize(new Dimension(newWidth, panel.getMinimumSize().height));
            }
        }
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    void scrollToBottom() {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    void showHelp() {
        addMessage("What is AnoNify?", "YOU");
        addMessage("AnoNify is an open-source, onion-secured platform for one-to-one communication.", "BOT");
        addMessage("How do I use it?", "YOU");
        addMessage("Provide your RSA private key and your peer's RSA public key.", "BOT");
        addMessage("For Tor users: PGP keys can be used as long as the key is RSA.", "BOT");
    }
}
