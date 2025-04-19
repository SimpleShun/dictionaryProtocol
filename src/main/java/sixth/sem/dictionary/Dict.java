package sixth.sem.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;

import sixth.sem.database.Database;

/**
 * Dict
 */
public class Dict {
    public static boolean loop = true;
    private final Database database;

    private ServerSocket serverSocket;

    public Dict(int port, String dbaddress, String username, String password, String table) {
        System.out.println("""
                #########################
                #   Dictionary Server   #
                #########################
                by: Rahul Gurung & Rohan Chaudhary
                """);

        database = new Database(dbaddress, username, password, table);

        System.out.println("Dictionary Server Running on Port:" + port);
        System.out.println("Now Listening...");

        try {
            serverSocket = new ServerSocket(port);

            while (loop) {
                Socket sock = serverSocket.accept();
                // new Thread(() -> {
                Thread.ofVirtual().start(() -> {
                    try {
                        handleSocket(sock);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSocket(Socket sock) throws IOException {
        Socket client = sock;
        BufferedReader reader;
        PrintWriter writer;
        String temp;
        Again loop = new Again(true);

        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        writer = new PrintWriter(client.getOutputStream(), true);

        writer.println("""
                #########################
                #   Dictionary Server   #
                #########################
                by: Rahul Gurung & Rohan Chaudhary
                    """);
        writer.flush();

        System.out.println(client.getInetAddress() + " accepted as client");

        while ((temp = reader.readLine()) != null) {
            temp = temp.trim();
            if (temp.isEmpty()) {
                continue;
            }
            writer.println(CmdHandler.handle(temp, database, loop));
            writer.flush();

            if (loop.loop == false) {
                System.out.println("for " + client.getInetAddress() + " Again.loop is " + loop.loop);
                break;
            }
        }
        if (reader != null)
            reader.close();
        if (writer != null)
            writer.close();
        if (client != null)
            client.close();
        System.out.println(client.getInetAddress() + " has been closed");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * InnerDict
 */
class Again {
    public boolean loop;

    Again(boolean l) {
        loop = l;
    }
}
