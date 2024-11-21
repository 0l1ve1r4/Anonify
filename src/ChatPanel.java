import javax.swing.*;

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
        JPanel messageContainer = MessageUtils.createMessagePanel(message, sender);
        chatPanel.add(messageContainer);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    public void showHelp() {
        addMessage("What is AnoNify?", "YOU");
        addMessage("AnoNify is an open-source, onion-secured platform for one-to-one communication.", "BOT");
        addMessage("How do I use it?", "YOU");
        addMessage("Provide your RSA private key and your peer's RSA public key.", "BOT");
        addMessage("For Tor users: PGP keys can be used as long as the key is RSA.", "BOT");
    }
}
