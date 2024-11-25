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

    public String getOnionAddress() throws IOException {
        File hostnameFile = new File(TOR_HOSTNAME_PATH);

        if (!hostnameFile.exists()) {
            throw new FileNotFoundException("The hostname file was not found. Is the Tor service running and configured?");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(hostnameFile))) {
            String onionAddress = reader.readLine();
            if (onionAddress == null || onionAddress.trim().isEmpty()) {
                throw new IOException("The hostname file is empty. Check the Tor configuration.");
            }
            return onionAddress;
        }
    }

    public void startChatServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started. Waiting for connections...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Client: " + inputLine);
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

            JOptionPane.showMessageDialog(null, "Connected to the server. Check the console for communication.", "Success", JOptionPane.INFORMATION_MESSAGE);

            new Thread(() -> {
                try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
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
}
