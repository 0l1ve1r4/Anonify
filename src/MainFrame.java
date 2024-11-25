import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    private final TorService torService = new TorService();

    public MainFrame() {
        setTitle("MainFrame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 600);
        setLayout(new BorderLayout());

        // Add components
        add(createLogoPanel(), BorderLayout.NORTH);
        ChatPanel chatPanel = new ChatPanel();
        add(chatPanel.getScrollPane(), BorderLayout.CENTER);
        add(new InputPanel(chatPanel), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLogoPanel() {
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
        settingsButton.addActionListener(e -> openSettingsWindow());
        iconsPanel.add(settingsButton);

        logoPanel.add(iconsPanel, BorderLayout.EAST);
        return logoPanel;
    }

    private void openSettingsWindow() {
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new GridLayout(3, 1));

        JButton configureOnionButton = new JButton("Configure Onion Server");
        configureOnionButton.addActionListener(e -> {
            try {
                String onionAddress = torService.getOnionAddress();
                JOptionPane.showMessageDialog(this, "Server Onion Address:\n" + onionAddress, "Onion Address", JOptionPane.INFORMATION_MESSAGE);
                new Thread(() -> {
                    try {
                        torService.startChatServer();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Failed to start the server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }).start();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton connectOnionButton = new JButton("Connect to an Onion Server");
        connectOnionButton.addActionListener(e -> {
            String onionAddress = JOptionPane.showInputDialog(this, "Enter Onion Server Address:", "Connect to Onion Server", JOptionPane.PLAIN_MESSAGE);
            if (onionAddress != null && !onionAddress.trim().isEmpty()) {
                try {
                    torService.connectToServer(onionAddress);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to connect to the server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
