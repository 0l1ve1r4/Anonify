import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MessageUtils {
    public static JPanel createMessagePanel(String message, String sender) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Constants.DARK_GRAY);
        containerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(sender.equals("YOU") ? Constants.SENDER_COLOR : Constants.RECIPIENT_COLOR);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel("<html><body style='width: 200px;'>" + formatMessage(message) + "</body></html>");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messagePanel.add(messageLabel);

        if (sender.equals("YOU")) {
            containerPanel.add(messagePanel, BorderLayout.EAST);
        } else {
            containerPanel.add(messagePanel, BorderLayout.WEST);
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
