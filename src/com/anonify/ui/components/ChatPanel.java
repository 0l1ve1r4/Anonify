package com.anonify.ui.components;

import java.awt.*;
import javax.swing.*;

import com.anonify.utils.Constants;
import com.anonify.utils.MessageUtils;

public class ChatPanel {
    private final JPanel chatPanel;
    private final JScrollPane scrollPane;
    private int messageIndex = 0;

    public ChatPanel() {
        chatPanel = new JPanel(new GridBagLayout()) {
            // Override the paintComponent method to set the background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  
                ImageIcon backgroundImage = new ImageIcon("src/res/bg_illustation.png"); 
                Image img = backgroundImage.getImage();

                int imgWidth = (img.getWidth(null)/2);
                int imgHeight = (img.getHeight(null)/2);

                int newWidth = (int) (imgWidth * 2);
                int newHeight = (int) (imgHeight * 2);

                int x = (getWidth() - newWidth) / 2;
                int y = (getHeight() - newHeight) / 2;

                g.drawImage(img, x, y, newWidth, newHeight, this);
            }
        };

        chatPanel.setBackground(Constants.DARK_GRAY);
        chatPanel.setForeground(Constants.DARK_GRAY);

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
        addMessage("What is Anonify?", "YOU");
        addMessage("Anonify is a quick chat application that works exclusively over the Tor network, connecting clients to a .onion address.", "BOT");
        addMessage("How do I use it?", "YOU");
        addMessage("To use Anonify, you must have Tor configured. This can be done by running the setup script located at scripts/tor.", "BOT");
        addMessage("Once configured, provide your RSA private key and your peer's RSA public key to establish a secure connection.", "BOT");
    }
    
}
