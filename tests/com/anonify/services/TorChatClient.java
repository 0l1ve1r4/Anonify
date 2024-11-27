import java.io.*;
import java.net.*;

public class TorChatClient {
    private static final String TOR_PROXY_HOST = "127.0.0.1";
    private static final int TOR_PROXY_PORT = 9050;
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            // Ask user for the .onion address
            System.out.print("Enter the .onion address of the server: ");
            String serverOnion = console.readLine();

            // Validate input
            if (serverOnion == null || serverOnion.isBlank()) {
                System.out.println("Invalid .onion address. Exiting.");
                return;
            }

            // Create Proxy for Tor
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(TOR_PROXY_HOST, TOR_PROXY_PORT));

            // Create socket with proxy
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
