package sixth.sem.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Dict
 */
public class Dict {
    // private final int port;
    private String temp;
    private boolean loop = true;

    private ServerSocket socket;
    private Socket client;

    private BufferedReader reader;
    private PrintWriter writer;

    public Dict(int port) {
        try {
            socket = new ServerSocket(port);
            while (loop) {
                client = socket.accept();
                handleSocket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null)
                    socket.close();
                if (client != null)
                    client.close();

                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSocket() throws IOException {
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        writer = new PrintWriter(client.getOutputStream(), true);

        while ((temp = reader.readLine()) != null) {
            temp = temp.trim();
            if (temp.equalsIgnoreCase("quit")) {
                loop = false;
                writer.write("221 Closing Connection");
                writer.flush();
                break;
            } else if (temp.isEmpty()) {
                continue;
            } else {
                writer.println(CmdHandler.handle(temp));
                // writer.write(CmdHandler.handle(temp) + "\n");
                writer.flush();
            }
        }
        if (reader != null)
            reader.close();
        if (writer != null)
            writer.close();
    }
}
