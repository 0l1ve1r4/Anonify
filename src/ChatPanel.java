import javax.swing.*;
import java.awt.*;

public class ChatPanel {
    private final JPanel chatPanel;
    private final JScrollPane scrollPane;

    public ChatPanel() {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Constants.DARK_GRAY);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);

        // Customize scroll bar
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setBackground(Constants.LIGHTER_GRAY);
        verticalBar.setUI(new CustomScrollBarUI());
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addMessage(String message, String sender) {
        // Create message panel
        JPanel messageContainer = MessageUtils.createMessagePanel(message, sender);

        // Prevent stretching by aligning components to the left
        messageContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Ensure consistent width
        messageContainer.setMaximumSize(new Dimension(chatPanel.getWidth(), Integer.MAX_VALUE));
        chatPanel.add(messageContainer);

        // Update the display
        chatPanel.revalidate();
        chatPanel.repaint();

        // Auto-scroll to the bottom
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    public void showHelp() {
        addMessage("What is AnoNify?", "YOU");
        addMessage("AnoNify is an open-source, onion-secured platform for one-to-one communication.", "BOT");
        addMessage("How do I use it?", "YOU");
        addMessage("Provide your RSA private key and your peer's RSA public key.", "BOT");
        addMessage("For Tor users: PGP keys can be used as long as the key is RSA.", "BOT");
    }
}
