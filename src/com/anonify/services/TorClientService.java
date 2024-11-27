package com.anonify.services;

import java.io.*;
import java.net.*;

class TorClientService {
    private static final String TOR_PROXY_HOST = "127.0.0.1";
    private static final int TOR_PROXY_PORT = 9050;
    private static final int SERVER_PORT = 12345;

    static void main(String serverOnion) {
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            if (serverOnion == null || serverOnion.isBlank()) {
                System.out.println("Invalid .onion address. Exiting.");
                return;
            }

            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(TOR_PROXY_HOST, TOR_PROXY_PORT));
            Socket socket = new Socket(proxy);
            InetSocketAddress serverAddress = new InetSocketAddress(serverOnion, SERVER_PORT);

            System.out.println("Connecting to " + serverOnion + " via Tor...");
            socket.connect(serverAddress);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String input;
                System.out.println("Connected. Type your message:");
                while ((input = console.readLine()) != null) {
                    out.println(input);
                    System.out.println("Server: " + in.readLine());
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
