package sixth.sem.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import sixth.sem.database.Database;

public class Dict {

    private static Database database = null;
    public static boolean loop = true;
    public static final Logger logger = Logger.getLogger(Dict.class.getCanonicalName());

    // public static final int threadpool_size =
    // Runtime.getRuntime().availableProcessors() * 2;
    /// private static ExecutorService executorService =
    // Executors.newVirtualThreadPerTaskExecutor();
    private static ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private static ServerSocket serverSocket;

    public Dict(int port, String dbaddress, String username, String password, String table) {
        logger.info("""
                \n
                #########################
                #   Dictionary Server   #
                #########################
                by: Rahul Gurung & Rohan Chaudhary
                """);

        database = new Database(dbaddress, username, password, table);

        logger.info("Dictionary Server Running on Port:" + port);
        logger.info("Now Listening...");

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(false);

            while (loop) {
                Socket sock = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        handleSocket(sock);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Couldn't handle Socket " + e.getMessage(), e);
                    }
                });
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed at ServerSocket Creation " + e.getMessage(), e);
            System.exit(-1);
        }
    }

    public static void terminate() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            database.stop();
            logger.info("Exited Successfully");
            System.exit(0);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed at Terminate" + e.getMessage(), e);
        }
    }

    private void handleSocket(Socket sock) throws IOException {
        Auth auth = new Auth();
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

        logger.info(client.getInetAddress() + " accepted as client");

        while ((temp = reader.readLine()) != null) {
            temp = temp.trim();
            if (temp.isEmpty()) {
                continue;
            }
            writer.println(CmdHandler.handle(temp, database, loop, auth));
            writer.flush();

            if (loop.loop == false) {
                logger.info("for " + client.getInetAddress() + " Again.loop is " + loop.loop);
                break;
            }
        }
        if (reader != null)
            reader.close();
        if (writer != null)
            writer.close();
        if (client != null) {
            client.close();
            logger.info(client.getInetAddress() + " has been closed");
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

class Auth {
    private boolean auth = false;

    public boolean getAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
}
