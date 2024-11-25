import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    static boolean firstRun = true;
    private final TorService torService = new TorService();

    public MainFrame() {
        setTitle("MainFrame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        ChatPanel chatPanel = new ChatPanel();

        add(createLogoPanel(chatPanel), BorderLayout.NORTH);
        add(chatPanel.getScrollPane(), BorderLayout.CENTER);
        add(new InputPanel(chatPanel), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLogoPanel(ChatPanel chatPanel) {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.DARK_GRAY);
        logoPanel.setPreferredSize(new Dimension(getWidth(), 50));
        logoPanel.setLayout(new BorderLayout());

        // Add logo and icons
        JLabel logoLabel = new JLabel(new ImageIcon("res/logo-no-bg-centralized.png"), SwingConstants.LEFT);
        logoPanel.add(logoLabel, BorderLayout.WEST);

        JPanel iconsPanel = new JPanel();
        iconsPanel.setBackground(Color.DARK_GRAY);
        iconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton settingsButton = createIconButton("res/settings-icon-white.png", "Settings");
        settingsButton.addActionListener(e -> openSettingsWindow(chatPanel));
        iconsPanel.add(settingsButton);

        logoPanel.add(iconsPanel, BorderLayout.EAST);
        return logoPanel;
    }

    private void openSettingsWindow(ChatPanel chatPanel) {
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new GridLayout(3, 1));

        JButton configureOnionButton = new JButton("Start a Onion Server");
        configureOnionButton.addActionListener(e -> {
            try {
                String onionAddress = torService.getOnionAddress(chatPanel);
                chatPanel.addMessage("Your Server Onion Address: " + onionAddress, "BOT");
                new Thread(() -> {
                    try {
                        torService.startChatServer(chatPanel);
                    } catch (IOException ex) {
                        chatPanel.addMessage("Failed to start the server: " + ex.getMessage(), "BOT");
                    }
                }).start();
            } catch (IOException ex) {
                chatPanel.addMessage(ex.getMessage(), "BOT");
            }
        });

        JButton connectOnionButton = new JButton("Connect to an Onion Server");
        connectOnionButton.addActionListener(e -> {
            String onionAddress = JOptionPane.showInputDialog(this, "Enter Onion Server Address:", "Connect to Onion Server", JOptionPane.PLAIN_MESSAGE);
            if (onionAddress != null && !onionAddress.trim().isEmpty()) {
                try {
                    torService.connectToServer(onionAddress, chatPanel);
                } catch (IOException ex) {
                    chatPanel.addMessage("Failed to connect to the server: " + ex.getMessage(), "BOT");
                }
            }
        });

        settingsFrame.add(configureOnionButton);
        settingsFrame.add(connectOnionButton);
        settingsFrame.setVisible(true);
    }

    private JButton createIconButton(String iconPath, String tooltip) {
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        button.setToolTipText(tooltip);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

}
