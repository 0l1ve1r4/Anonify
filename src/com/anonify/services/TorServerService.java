package com.anonify.services;

import java.io.*;
import java.net.*;

import com.anonify.ui.ChatPanel;

class TorServerService {
    private static final int PORT = 12345;
    private static final String HIDDEN_SERVICE_HOSTNAME_FILE = "/var/lib/tor/hidden_service/hostname";

    static void main(ChatPanel chatPanel) {
        try {
            String onionAddress = readOnionAddress(HIDDEN_SERVICE_HOSTNAME_FILE, chatPanel);
            if (onionAddress == null) {
                System.err.println("Could not read the .onion address. Make sure Tor is configured and running.");
                chatPanel.addMessage("Could not read the .onion address. Make sure Tor is configured and running.", "BOT");
                return;
            }

            System.out.println("TorChat server is starting...");
            System.out.println("Your .onion address is: " + onionAddress);
            System.out.println("Clients can connect to this address via Tor.");

            chatPanel.addMessage("TorChat server is starting...", "BOT");
            chatPanel.addMessage("Your .onion address is: " + onionAddress, "BOT");
            chatPanel.addMessage("Clients can connect to this address via Tor.", "BOT");

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server is listening on port: " + PORT);
                chatPanel.addMessage("Server is listening on port: " + PORT, "BOT");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clientSocket, chatPanel)).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readOnionAddress(String filePath, ChatPanel chatPanel) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine(); // Return the first line, which is the .onion address
        } catch (IOException e) {
            System.err.println("Error reading the .onion address: " + e.getMessage());
            chatPanel.addMessage("Error reading the .onion address: " + e.getMessage(), "BOT");
            return null;
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ChatPanel chatPanel;

    ClientHandler(Socket socket, ChatPanel chatPanel) {
        this.clientSocket = socket;
        this.chatPanel = chatPanel;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client: " + message);
                out.println("Server: " + message); // Echo message

                chatPanel.addMessage("Client: " + message, "BOT");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
