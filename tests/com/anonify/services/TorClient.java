import java.io.*;
import java.net.*;

public class TorClient {
    public static void main(String[] args) {
        String torProxyHost = "127.0.0.1";
        int torProxyPort = 9050;
        String serverHost = "5akylqy3v2db2i6kt6u72nfbqf46wth44e6tr7mmh4uidgqj6fifw4qd.onion";
        int serverPort = 12345;

        try {
            // Configurando o proxy SOCKS
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(torProxyHost, torProxyPort));

            // Conectando ao servidor .onion através do Tor
            Socket socket = new Socket(proxy);
            socket.connect(new InetSocketAddress(serverHost, serverPort));

            // Enviando dados ao servidor
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println("Hello from TorClient!");

            // Lendo a resposta
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            System.out.println("Server response: " + reader.readLine());

            // Fechando a conexão
            socket.close();
        } catch (Exception e) {
            System.out.println(serverHost);
            e.printStackTrace();
        }
    }
}
