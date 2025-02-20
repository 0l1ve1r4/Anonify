package com.anonify.services;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.concurrent.CopyOnWriteArrayList;

import com.anonify.ui.components.ChatPanel;

class TorServerService {
    private static int PORT; // = 12345;
    private static String HIDDEN_SERVICE_HOSTNAME_FILE; // = "/var/lib/tor/hidden_service/hostname";
    private static final CopyOnWriteArrayList<PrintWriter> clients = new CopyOnWriteArrayList<>();
    private static String onionAddressGlobal;

    static void main(String hostFilepath, int port, ChatPanel chatPanel) {
        HIDDEN_SERVICE_HOSTNAME_FILE = hostFilepath;
        PORT = port;
        
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

            onionAddressGlobal = onionAddress;

            chatPanel.addMessage("TorChat server is starting...", "BOT");
            chatPanel.addMessage("Your .onion address is: " + onionAddress, "BOT");
            chatPanel.addMessage("Clients can connect to this address via Tor.", "BOT");

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server is listening on port: " + PORT);
                chatPanel.addMessage("Server is listening on port: " + PORT, "BOT");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    clients.add(out);

                    new Thread(new ClientHandler(clientSocket, chatPanel, out)).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public String getAddress(){
        return onionAddressGlobal;
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

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ChatPanel chatPanel;
        private final PrintWriter clientOut;

        ClientHandler(Socket socket, ChatPanel chatPanel, PrintWriter clientOut) {
            this.clientSocket = socket;
            this.chatPanel = chatPanel;
            this.clientOut = clientOut;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Client: " + message);
                    Instant now = Instant.now();

                    chatPanel.addMessage("["+ now.toString() + "]: " + message, "BOT");
        
                    // Broadcast the message to all clients except the sender
                    for (PrintWriter out : clients) {
                        if (out != clientOut) {
                            out.println(message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(clientOut);
            }
        }
        
    }
}
