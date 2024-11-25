import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class TorService {
    private static final String TOR_HOSTNAME_PATH = "/var/lib/tor/hidden_service/hostname";
    private static final String TOR_PROXY_HOST = "127.0.0.1";
    private static final int TOR_PROXY_PORT = 9050;
    private static final int SERVER_PORT = 12345;
    private ChatPanel chatPanel = new ChatPanel();


    public String getOnionAddress() throws IOException {
        File hostnameFile = new File(TOR_HOSTNAME_PATH);

        if (!hostnameFile.exists()) {
            chatPanel.addMessage("The hostname file was not found. Is the Tor service running and configured?", "BOT");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(hostnameFile))) {
            String onionAddress = reader.readLine();
            if (onionAddress == null || onionAddress.trim().isEmpty()) {
                chatPanel.addMessage("The hostname file is empty. Check the Tor configuration.", "BOT");
            }
            return onionAddress;
        }
    }

    public void startChatServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            chatPanel.addMessage("Server started. Waiting for connections...", "BOT");
            Socket clientSocket = serverSocket.accept();
            chatPanel.addMessage("Client connected!", "BOT");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    chatPanel.addMessage(inputLine, "BOT"); // TODO: Change bot to anon(another user)
                    if ("bye".equalsIgnoreCase(inputLine)) break;
                    out.println("Server: " + inputLine);
                }
            }
        }
    }

    public void connectToServer(String onionAddress) throws IOException {
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("socksProxyHost", TOR_PROXY_HOST);
        systemProperties.setProperty("socksProxyPort", String.valueOf(TOR_PROXY_PORT));

        try (Socket socket = new Socket(onionAddress, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            new Thread(() -> {
                try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
                    String userInput;
                    chatPanel.addMessage("Connected to server. Type your messages.", "BOT");
                    while ((userInput = stdIn.readLine()) != null) {
                        out.println(userInput);
                        chatPanel.addMessage(in.readLine(), "BOT");
                        if ("bye".equalsIgnoreCase(userInput)) break;
                    }
                } catch (IOException e) {
                    chatPanel.addMessage("Error during communication: " + e.getMessage(), "BOT");
                }
            }).start();
        }
    }
}
