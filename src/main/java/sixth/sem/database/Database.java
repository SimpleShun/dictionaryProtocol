package sixth.sem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database
 */
public final class Database {
    private final String name_of_table;
    private Connection connection;
    private PreparedStatement statement;

    public Database(String databaseAddr, String username, String password, String tablename) {
        name_of_table = tablename;
        try {
            if ((connection = DriverManager.getConnection(databaseAddr, username, password)) != null) {
                System.out.println("Connected to Database Successfully");
            } else {
                System.err.println("Failed to create Database Connection");
                System.err.println("Aborting....");
                System.exit(-1);
            }

        } catch (SQLException e) {
            System.err.println("Failed to create Database Connection");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void stop() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String define(String[] word) {
        String temp = "";
        try {
            statement = connection.prepareStatement("SELECT * FROM " + name_of_table + " WHERE word= ?");
            for (int i = 1; i < word.length; i++) {
                statement.setString(1, word[i]);
                ResultSet result = statement.executeQuery();
                int loops_to_help_know_if_resultset_is_empty = 0;
                while (result.next()) {
                    ++loops_to_help_know_if_resultset_is_empty;
                    temp = temp.concat(word[i] + " -> " + result.getString("value") + "\n");
                }
                if (loops_to_help_know_if_resultset_is_empty == 0) {
                    temp = temp.concat(word[i] + "-> " + "not in the Database\n");
                }
            }
        } catch (SQLException e) {
            temp = "Failed to perform the action";
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    // add {WORD} {DEFINATION}
    public String add(String[] word) {
        String temp = "";
        try {
            statement = connection.prepareStatement("INSERT INTO " + name_of_table + " VALUES(? , ?)");
            statement.setString(1, word[1]);
            statement.setString(2, rest(word));

            if (statement.executeUpdate() == 1) {
                temp = word[1] + " was Successfully added";
            } else {
                temp = "Failed to add " + word[1];
            }

        } catch (SQLException e) {
            temp = "Failed to peform action";
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    // REMOVE word
    public String remove(String[] word) {
        String temp = "";
        try {
            statement = connection.prepareStatement("DELETE FROM " + name_of_table + " WHERE word=?");
            statement.setString(1, word[1]);

            if (statement.executeUpdate() == 1) {
                temp = word[1] + " was Successfully removed";
            } else {
                temp = "Failed to remove" + word[1];
            }

        } catch (SQLException e) {
            temp = "Failed to peform action";
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    private String rest(String[] word) {
        String temp = "";
        for (int i = 2; i < word.length; i++) {
            temp = temp.concat(" " + word[i]);
        }
        return temp;
    }
}
