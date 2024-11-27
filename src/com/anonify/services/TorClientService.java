package com.anonify.services;

import java.io.*;
import java.net.*;

import com.anonify.ui.ChatPanel;

class TorClientService {
    private static final String TOR_PROXY_HOST = "127.0.0.1";
    private static final int TOR_PROXY_PORT = 9050;
    private static final int SERVER_PORT = 12345;

    static void main(String serverOnion, ChatPanel chatPanel) {
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
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

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String input;
                System.out.println("Connected");
                chatPanel.addMessage("Connected", "BOT");
                while ((input = console.readLine()) != null) {
                    out.println(input);
                    System.out.println("Server: " + in.readLine());
                    chatPanel.addMessage(("Server: " + in.readLine()), "BOT");

                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
