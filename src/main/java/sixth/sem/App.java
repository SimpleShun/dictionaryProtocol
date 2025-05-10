package sixth.sem;

import java.util.logging.Level;
import java.util.stream.Stream;

import sixth.sem.dictionary.Dict;
import sixth.sem.fileHandler.ReadConfigV2;

public class App {
    private static String username;
    private static String password;

    public static void main(String[] args) {
        var data_map = ReadConfigV2.setup();
        var fields = Stream.of(
                "port",
                "databaseaddr",
                "username",
                "password",
                "name_of_table")
                .takeWhile(data_map::containsKey)
                .count();

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
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
