import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class TorService {
    private static final String TOR_HOSTNAME_PATH = "/var/lib/tor/hidden_service/hostname";
    private static final String TOR_PROXY_HOST = "127.0.0.1";
    private static final int TOR_PROXY_PORT = 9050;
    private static final int SERVER_PORT = 12345;

    public String getOnionAddress(ChatPanel panel) throws IOException {
        File hostnameFile = new File(TOR_HOSTNAME_PATH);

        if (!hostnameFile.exists()) {
            panel.addMessage("The hostname file was not found. Is the Tor service running and configured?", "BOT");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(hostnameFile))) {
            String onionAddress = reader.readLine();
            if (onionAddress == null || onionAddress.trim().isEmpty()) {
                panel.addMessage("The hostname file is empty. Check the Tor configuration.", "BOT");
            }
            return onionAddress;
        }
    }

    public void startChatServer(ChatPanel chatPanel) throws IOException {
        chatPanel.addMessage("Trying to start the server at port:" + SERVER_PORT, "BOT");
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            chatPanel.addMessage("Server started. Waiting for connections...", "BOT");
            Socket clientSocket = serverSocket.accept();
            chatPanel.addMessage("Client connected!", "BOT");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // Printing received message on CLI and sending back to client
                    System.out.println("Received from client: " + inputLine); // Print received message on CLI
                    chatPanel.addMessage(inputLine, "USER"); // Display it as a message from the USER (client)

                    out.println("Server: " + inputLine); // Echo the message back to the client

                    if ("bye".equalsIgnoreCase(inputLine)) break;
                }
            }
        }
    }

    public void connectToServer(String onionAddress, ChatPanel panel) throws IOException {
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("socksProxyHost", TOR_PROXY_HOST);
        systemProperties.setProperty("socksProxyPort", String.valueOf(TOR_PROXY_PORT));

        try (Socket socket = new Socket(onionAddress, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            new Thread(() -> {
                try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
                    String userInput;
                    panel.addMessage("Connected to server. Type your messages.", "BOT");
                    while ((userInput = stdIn.readLine()) != null) {
                        out.println(userInput);
                        panel.addMessage(userInput, "USER"); // Print message sent by the user
                        System.out.println("Sent to server: " + userInput); // Print message sent on CLI

                        String serverResponse = in.readLine();
                        panel.addMessage(serverResponse, "SERVER"); // Print server response
                        System.out.println("Received from server: " + serverResponse); // Print received message on CLI

                        if ("bye".equalsIgnoreCase(userInput)) break;
                    }
                } catch (IOException e) {
                    panel.addMessage("Error during communication: " + e.getMessage(), "BOT");
                }
            }).start();
        }
    }
}
