import java.io.*;
import java.net.*;

public class TorChatServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado. Aguardando conexões...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado!");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Cliente: " + inputLine);
                if ("bye".equalsIgnoreCase(inputLine)) break;
                out.println("Servidor: " + inputLine);
            }
        }
    }
}
