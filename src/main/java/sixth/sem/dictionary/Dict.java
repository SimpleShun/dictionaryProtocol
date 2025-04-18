package sixth.sem.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Dict
 */
public class Dict {
    private final ServerSocket socket;
    private Socket client;

    Dict(ServerSocket sock) throws IOException {
        socket = sock;
        while (true) {
            client = sock.accept();
            handleSocket();
        }
    }

    void handleSocket() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String temp;
        while (true) {
            System.out.println(reader.readLine());
        }
    }
}
