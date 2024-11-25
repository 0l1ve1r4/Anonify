import java.io.*;
import java.net.*;
import java.util.Properties;

public class TorChatClient {
    public static void main(String[] args) throws IOException {
        String torProxyHost = "127.0.0.1";
        int torProxyPort = 9050;
        String serverOnionAddress = "servidor.onion";
        int serverPort = 12345;

        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("socksProxyHost", torProxyHost);
        systemProperties.setProperty("socksProxyPort", String.valueOf(torProxyPort));

        try (Socket socket = new Socket(serverOnionAddress, serverPort)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            System.out.println("Conectado ao servidor. Digite suas mensagens:");
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Servidor: " + in.readLine());
                if ("bye".equalsIgnoreCase(userInput)) break;
            }
        }
    }
}
