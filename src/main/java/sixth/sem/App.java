package sixth.sem;

import java.util.logging.Level;
import java.util.stream.Stream;

import sixth.sem.dictionary.Dict;
import sixth.sem.fileHandler.ReadConfigV2;

public class App {
    // private static int port;
    // private static String databaseAddr;
    // private static String name_of_table;
    private static String username;
    private static String password;

    public static void main(String[] args) {
        // ReadConfig.canPopulate();
        var data_map = ReadConfigV2.setup();
        var fields = Stream.of(
                "port",
                "databaseaddr",
                "username",
                "password",
                "name_of_table")
                .takeWhile((s) -> {
                    return data_map.containsKey(s);
                }).count();

        if (fields == 5) {
            username = data_map.get("username");
            password = data_map.get("password");

            new Dict(Integer.parseInt(data_map.get("port")),
                    data_map.get("databaseaddr"),
                    data_map.get("username"),
                    data_map.get("password"),
                    data_map.get("name_of_table"));
        } else {
            Dict.logger.log(Level.SEVERE, "Missing Info");
            Dict.logger.log(Level.SEVERE, "Aborting..");
            System.exit(-1);
        }
        // if (port == -1) {
        // Dict.logger.log(Level.SEVERE, "Port number not specified");
        // Dict.logger.log(Level.SEVERE, "Aborting..");
        // System.exit(-1);
        // }
        // if (databaseAddr == null || username == null || name_of_table == null ||
        // password == null) {
        // Dict.logger.log(Level.SEVERE, "Missing Info");
        // Dict.logger.log(Level.SEVERE, "Aborting..");
        // System.exit(-1);
        // }
        // new Dict(port, databaseAddr, username, password, name_of_table);
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    // public static void setPort(int port) {
    // App.port = port;
    // }
    //
    // public static void setDatabaseAddr(String databaseAddr) {
    // App.databaseAddr = databaseAddr;
    // }
    //
    // public static void setUsername(String username) {
    // App.username = username;
    // }
    //
    // public static void setPassword(String password) {
    // App.password = password;
    // }
    //
    // public static void setName_of_table(String name_of_table) {
    // App.name_of_table = name_of_table;
    // }
    //
}
