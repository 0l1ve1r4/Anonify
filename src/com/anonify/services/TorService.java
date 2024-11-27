package com.anonify.services;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import com.anonify.ui.ChatPanel;

public class TorService {
    private static final String TOR_HOSTNAME_PATH = "/var/lib/tor/hidden_service/hostname";
    private static final String TOR_PROXY_HOST = "127.0.0.1";
    private static final int TOR_PROXY_PORT = 9050;
    private static final int SERVER_PORT = 9001;

    private ChatPanel userInterface;

    public TorService(ChatPanel userInterface) {
        this.userInterface = userInterface;
    }

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
            System.out.println(onionAddress);
            return onionAddress;
        }
    }

    public void startChatServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
                userInterface.addMessage("Server started, waiting for connection.", "BOT");
                Socket clientSocket = serverSocket.accept();
                userInterface.addMessage("Client connected!", "BOT");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        userInterface.addMessage(inputLine, "CLIENT");
                        if ("bye".equalsIgnoreCase(inputLine)) break;

                        // Echo the message back to the client
                        out.println(inputLine);
                        userInterface.addMessage(inputLine, "SERVER");
                    }
                }
            } catch (IOException e) {
                userInterface.addMessage("Error in server: " + e.getMessage(), "BOT");
            }
        }).start();
    }

    public void connectToServer(String onionAddress) {
        new Thread(() -> {
            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("socksProxyHost", TOR_PROXY_HOST);
            systemProperties.setProperty("socksProxyPort", String.valueOf(TOR_PROXY_PORT));

            try (Socket socket = new Socket(onionAddress, SERVER_PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

                userInterface.addMessage("Connected to the server.", "BOT");
                String userInput;

                while ((userInput = stdIn.readLine()) != null) {
                    userInterface.addMessage(userInput, "USER");
                    out.println(userInput);

                    String serverResponse = in.readLine();
                    userInterface.addMessage(serverResponse, "SERVER");
                    if ("bye".equalsIgnoreCase(userInput)) break;
                }
            } catch (IOException e) {
                userInterface.addMessage("Error in client: " + e.getMessage(), "BOT");
            }
        }).start();
    }
}
