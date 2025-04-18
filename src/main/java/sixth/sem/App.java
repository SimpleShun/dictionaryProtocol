package sixth.sem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;

/**
 * Hello world!
 */
public class App {
    private ServerSocket serverSocket;
    private static String address;
    private static int port = -1;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("App address port");
            System.exit(-1);
        } else {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Port number is not valid");
                System.exit(-1);
            }
        }
        address = args[0];
        new App();
    }

    public App() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress());
                HandleSocket(clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    void HandleSocket(Socket sc) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(sc.getInputStream()));
        // URL oorl = URL.of(URI.create(reader.readLine()), null);
        System.out.println(reader.readLine());
    }
}
