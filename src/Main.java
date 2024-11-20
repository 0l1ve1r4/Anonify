import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class Main {
    public static String jframeTitle = "No-Connected"; 
        // Colors (ChatGPT-like colors)
    public static Color darkGray = new Color(52, 53, 65);  // #343541
    public static Color lighterGray = new Color(68, 70, 84);  // #444654
    public static Color lightGray = new Color(234, 234, 234);  // #EAEAEA
    public static Color blue = new Color(0, 140, 186);   // #008CBA


    public static void main(String[] args) {
        // Create main frame
        JFrame frame = new JFrame(jframeTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());

        Color textColor = lightGray;

        // Logo panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(darkGray);
        logoPanel.setPreferredSize(new Dimension(frame.getWidth(), 100));
        logoPanel.setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Load the image
        ImageIcon originalIcon = new ImageIcon("res/logo-no-bg-centralized.png"); // Replace with your actual logo file path
        
        // Resize the image to a smaller size (e.g., 100x100)
        Image img = originalIcon.getImage();
        Image resizedImg = img.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
        
        // Set the resized image as the icon
        logoLabel.setIcon(new ImageIcon(resizedImg));
        
        logoPanel.add(logoLabel, BorderLayout.CENTER);

        // Chat display area
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS)); // To stack messages vertically
        chatPanel.setBackground(darkGray);

        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Message input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(lighterGray);

        // Updated JTextField with a box-like appearance
        JTextField messageField = new JTextField();
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageField.setBackground(lighterGray);
        messageField.setForeground(textColor);
        messageField.setCaretColor(textColor);

        // Custom border to make it look like a box
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);  // Padding inside the text field
        Border border = BorderFactory.createLineBorder(lighterGray, 2);      // Border color and thickness
        messageField.setBorder(BorderFactory.createCompoundBorder(border, padding));

        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBackground(blue);
        sendButton.setForeground(textColor);
        sendButton.setFocusPainted(false);

        // Add components to input panel
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add components to frame
        frame.add(logoPanel, BorderLayout.NORTH);
        frame.add(chatScrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Action for sending a message
        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                addMessageToChat(chatPanel, message, "YOU"); // Add message inside a box
                messageField.setText(""); // Clear input field

                // Simulate sending the message to a server or peer (placeholder logic)
                sendMessageToPeer(message);
            }
        });

        // Key listener for pressing Enter to send the message
        messageField.addActionListener(e -> sendButton.doClick()); // Trigger the send button action

        // Show frame
        frame.setVisible(true);
    }

    // Method to add message inside a box
    private static void addMessageToChat(JPanel chatPanel, String message, String sender) {
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(darkGray);
        messagePanel.setBorder(BorderFactory.createLineBorder(lighterGray, 2)); // Border around each message

        JLabel messageLabel = new JLabel("<html><body style='width: 200px;'>" + sender + ": " + message + "</body></html>");
        messageLabel.setForeground(lightGray);
        messagePanel.add(messageLabel);

        chatPanel.add(messagePanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    // Placeholder method to simulate sending a message
    private static void sendMessageToPeer(String message) {
        // Replace with real networking logic for sending the message
        System.out.println("Message sent to peer: " + message);
    }
}
