import java.io.*;
import java.net.*;

public class TorChatClient {
    private static final String TOR_PROXY_HOST = "127.0.0.1";
    private static final int TOR_PROXY_PORT = 9050;
    private static final String SERVER_ONION = "yxjoli2zwc6zgulygf3tw4wuohq3lvfojbcqyzvuim3gv54se4txc7yd.onion";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            // Create Proxy for Tor
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(TOR_PROXY_HOST, TOR_PROXY_PORT));
            
            // Create socket with proxy
            Socket socket = new Socket(proxy);
            InetSocketAddress serverAddress = new InetSocketAddress(SERVER_ONION, SERVER_PORT);

            System.out.println("Connecting to " + SERVER_ONION + " via Tor...");
            socket.connect(serverAddress);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

                String input;
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
