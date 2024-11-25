import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class MainFrame extends JFrame {
    static boolean firstRun = true;

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

        // Add logo on the left
        ImageIcon originalIcon = new ImageIcon("res/logo-no-bg-centralized.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(resizedImage), SwingConstants.LEFT);
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        logoPanel.add(logoLabel, BorderLayout.WEST);

        // Add icons panel on the right
        JPanel iconsPanel = new JPanel();
        iconsPanel.setBackground(Color.DARK_GRAY);
        iconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // Align icons to the right

        // Add the settings icon with functionality
        JButton settingsButton = createIconButton("res/settings-icon-white.png", "Settings");
        settingsButton.addActionListener(e -> openSettingsWindow());
        iconsPanel.add(settingsButton);

        iconsPanel.add(createIconButton("res/privacy-icon-white.png", "Privacy"));
        iconsPanel.add(createIconButton("res/help-icon-white.png", "Help"));

        logoPanel.add(iconsPanel, BorderLayout.EAST);

        return logoPanel;
    }

    private void openSettingsWindow() {
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new GridLayout(3, 1));

        JButton configureOnionButton = new JButton("Configure Onion Server");
        JButton connectOnionButton = new JButton("Connect to an Onion Server");

        configureOnionButton.addActionListener(e -> showOnionServerAddress());
        connectOnionButton.addActionListener(e -> openOnionConnectionInput());

        settingsFrame.add(configureOnionButton);
        settingsFrame.add(connectOnionButton);

        settingsFrame.setVisible(true);
}

    // Show the Onion Server Address and Start the Server
    private void showOnionServerAddress() {
        // Path to the Tor hostname file
        File hostnameFile = new File("/var/lib/tor/hidden_service/hostname");

        if (!hostnameFile.exists()) {
            JOptionPane.showMessageDialog(this, "The hostname file was not found. Is the Tor service running and configured?", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(hostnameFile))) {
            String onionAddress = reader.readLine();

            if (onionAddress == null || onionAddress.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "The hostname file is empty. Check the Tor configuration.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Show the onion address
            JOptionPane.showMessageDialog(this, "Server Onion Address:\n" + onionAddress, "Onion Address", JOptionPane.INFORMATION_MESSAGE);

            // Start the server in a background thread
            new Thread(() -> {
                try {
                    startTorChatServer();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to start the server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the hostname file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Start the Tor Chat Server
    private void startTorChatServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started. Waiting for connections...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client: " + inputLine);
                if ("bye".equalsIgnoreCase(inputLine)) break;
                out.println("Server: " + inputLine);
            }
        }
    }


    private void openOnionConnectionInput() {
        String onionAddress = JOptionPane.showInputDialog(this, "Enter Onion Server Address:", "Connect to Onion Server", JOptionPane.PLAIN_MESSAGE);

        if (onionAddress != null && !onionAddress.trim().isEmpty()) {
            try {
                connectToOnionServer(onionAddress);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to connect to the server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Connect to the Onion Server
    private void connectToOnionServer(String onionAddress) throws IOException {
        String torProxyHost = "127.0.0.1";
        int torProxyPort = 9050;
        int serverPort = 12345;

        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("socksProxyHost", torProxyHost);
        systemProperties.setProperty("socksProxyPort", String.valueOf(torProxyPort));

        try (Socket socket = new Socket(onionAddress, serverPort)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            JOptionPane.showMessageDialog(this, "Connected to the server. Check the console for communication.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // You can also extend this to include a GUI-based chat window
            new Thread(() -> {
                try {
                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                    String userInput;

                    System.out.println("Connected to server. Type your messages:");
                    while ((userInput = stdIn.readLine()) != null) {
                        out.println(userInput);
                        System.out.println("Server: " + in.readLine());
                        if ("bye".equalsIgnoreCase(userInput)) break;
                    }
                } catch (IOException e) {
                    System.err.println("Error during communication: " + e.getMessage());
                }
            }).start();
        }
    }

    // Utility method to create a button with an icon
    private JButton createIconButton(String iconPath, String tooltip) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image resizedImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(resizedImage));
        button.setToolTipText(tooltip);
        button.setContentAreaFilled(false); // Transparent background
        button.setBorderPainted(false); // No border
        button.setFocusPainted(false); // No focus border
        return button;
    }

}
