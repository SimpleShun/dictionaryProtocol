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
    private final String welcome_text = """
            \n
            #########################
            #   Dictionary Server   #
            #########################
            by: Rahul Gurung & Rohan Chaudhary
            """;
    private static Database database = null;
    public static boolean loop = true;
    public static final Logger logger = Logger.getLogger(Dict.class.getCanonicalName());
    private static final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public Dict(int port, String dbaddress, String username, String password, String table) {
        database = new Database(dbaddress, username, password, table);
        logger.info(welcome_text);
        logger.info("Dictionary Server Listening on Port:" + port);

        try (var serverSocket = new ServerSocket(port);) {
            while (loop) {
                Socket sock = serverSocket.accept();
                executorService.submit(() -> {
                    handleSocket(sock);
                });
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed at ServerSocket Creation " + e.getMessage(), e);
            System.exit(-1);
        }
    }

    public static void terminate() {
        database.stop();
        logger.info("Exited Successfully");
        System.exit(0);
    }

    private void handleSocket(Socket sock) {
        String temp;
        Socket client = sock;
        Auth auth = new Auth();
        Again loop = new Again(true);

        try (var reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                var writer = new PrintWriter(client.getOutputStream(), true);) {
            logger.info(client.getInetAddress() + " accepted as client");
            writer.println(welcome_text);

            while ((temp = reader.readLine()) != null) {
                temp = temp.trim();
                if (temp.isEmpty()) {
                    continue;
                }
                var response = CmdHandler.handle(temp, database, loop, auth);
                writer.println(response + "\r\n");

                if (loop.loop == false) {
                    logger.info("for " + client.getInetAddress() + " Again.loop is " + loop.loop);
                    break;
                }
            }
            if (client != null) {
                client.close();
                logger.info(client.getInetAddress() + " has been closed");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException encountered" + e.getMessage(), e);
        }
    }
}

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
