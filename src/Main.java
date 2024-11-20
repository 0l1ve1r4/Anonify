import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static boolean debugMode = true;

    public static void main(String[] args) {
        JFrame frame = new JFrame("AnoNify - Secure Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JTextField messageField = new JTextField();
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(chatScrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    chatArea.append("You: " + message + "\n"); 
                    messageField.setText("");

                    sendMessageToPeer(message);
                }
            }
        });

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick(); 
            }
        });

        frame.setVisible(true);
    }

    private static void sendMessageToPeer(String message) {

        if (debugMode) {
            System.out.println("Message sent to peer: " + message);            
        }
    }
}
