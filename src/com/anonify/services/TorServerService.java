package com.anonify.services;

import java.io.*;
import java.net.*;

class TorServerService {
    private static final int PORT = 12345;
    private static final String HIDDEN_SERVICE_HOSTNAME_FILE = "/var/lib/tor/hidden_service/hostname";

    static void main() {
        try {
            String onionAddress = readOnionAddress(HIDDEN_SERVICE_HOSTNAME_FILE);
            if (onionAddress == null) {
                System.err.println("Could not read the .onion address. Make sure Tor is configured and running.");
                return;
            }

            System.out.println("TorChat server is starting...");
            System.out.println("Your .onion address is: " + onionAddress);
            System.out.println("Clients can connect to this address via Tor.");

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server is listening on port: " + PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readOnionAddress(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine(); // Return the first line, which is the .onion address
        } catch (IOException e) {
            System.err.println("Error reading the .onion address: " + e.getMessage());
            return null;
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client: " + message);
                out.println("Server: " + message); // Echo message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
