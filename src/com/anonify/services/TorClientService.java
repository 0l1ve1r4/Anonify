package com.anonify.services;

import java.io.*;
import java.net.*;

import com.anonify.ui.components.ChatPanel;

class TorClientService {
    private static String TOR_PROXY_HOST; // = "127.0.0.1";
    private static int TOR_PROXY_PORT; // = 9050;
    private static int SERVER_PORT;  // = 12345;

    // Global private variable for PrintWriter
    private static PrintWriter out;

    static void main(String serverOnion, String torProxyHost, int torProxyPort, int serverPort, ChatPanel chatPanel) {
        TOR_PROXY_HOST = torProxyHost;
        TOR_PROXY_PORT = torProxyPort;
        SERVER_PORT = serverPort;
        
        try {
            if (serverOnion == null || serverOnion.isBlank()) {
                System.out.println("Invalid .onion address. Exiting.");
                chatPanel.addMessage("Invalid .onion address. Exiting.", "BOT");
                return;
            }

            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(TOR_PROXY_HOST, TOR_PROXY_PORT));
            Socket socket = new Socket(proxy);
            InetSocketAddress serverAddress = new InetSocketAddress(serverOnion, SERVER_PORT);

            System.out.println("Connecting to " + serverOnion + " via Tor...");
            chatPanel.addMessage("Connecting to " + serverOnion + " via Tor...", "BOT");

            socket.connect(serverAddress);

            out = new PrintWriter(socket.getOutputStream(), true);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

                System.out.println("Connected");
                chatPanel.addMessage("Connected", "BOT");

                // Thread to read server messages
                Thread readerThread = new Thread(() -> {
                    String response;
                    try {
                        while ((response = in.readLine()) != null) {
                            System.out.println("Server: " + response);
                            chatPanel.addMessage(response, "BOT");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                readerThread.start();

                // Main thread for user input
                String input;
                while ((input = console.readLine()) != null) {
                    sendMessage(input, "You");
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void sendMessage(String message, String sender) {
        if (out == null) {
            System.err.println("Error: PrintWriter is not initialized.");
            return;
        }

        if (message == null || message.isBlank()) return;

        String formattedMessage = sender + ": " + message;
        out.println(formattedMessage);
        System.out.println("Sent: " + formattedMessage);
    }
}

