package sixth.sem;

import sixth.sem.dictionary.Dict;

/**
 * Hello world!
 */
public class App {
    private static int port = 2628;

    private final static String databaseAddr = "jdbc:mariadb://localhost:3306/dictionary";
    private final static String username = "dict";
    private final static String password = "123";
    private final static String name_of_table = "dictionary";

    public static void main(String[] args) {
        new Dict(port, databaseAddr, username, password, name_of_table);
    }
}
