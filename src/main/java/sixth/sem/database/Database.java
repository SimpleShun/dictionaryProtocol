package sixth.sem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database
 */
public final class Database {
    private final String databaseAddr = "";
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(databaseAddr);
        } catch (SQLException e) {
            System.err.println("Failed to create Database Connection");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void define(String word) {
        try {
            var prepared_statement = connection.prepareStatement("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Will start a repl to read line
    public void add(String word) {

    }
}
