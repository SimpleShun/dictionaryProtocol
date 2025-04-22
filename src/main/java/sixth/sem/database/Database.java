package sixth.sem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import sixth.sem.dictionary.Dict;
import sixth.sem.Response;

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
            connection = DriverManager.getConnection(databaseAddr, username, password);
            Dict.logger.info("Connected Successfully to Database on " + databaseAddr);
            Dict.logger.info("using TABLE " + tablename + " as USER: " + username);
        } catch (SQLException e) {
            Dict.logger.log(Level.SEVERE, "Failed to create Database Connection " + e.getMessage(), e);
            Dict.logger.info("Aborting....");
            System.exit(-1);
        }
    }

    public void stop() {
        try {
            if (connection != null)
                connection.close();
            Dict.logger.info("Database Connection Closed");
        } catch (SQLException e) {
            Dict.logger.log(Level.WARNING, "failed to close connection" + e.getMessage(), e);
        }
    }

    public Response showDb(String[] word) {
        String temp = "";
        Response response = new Response();

        try {
            statement = connection.prepareStatement("SHOW DATABASES");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(1).equals("information_schema"))
                    continue;
                temp = temp.concat(resultSet.getString(1) + "\n");
            }
        } catch (SQLException e) {
            temp = "Failed to perform the action";
            Dict.logger.log(Level.WARNING, "couldn't perform action " + e.getMessage(), e);
        }
        response.responseCode = 200;
        response.data = temp;
        return response;
    }

    public Response define(String[] word) {
        String temp = "";
        Response response = new Response();
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
            Dict.logger.log(Level.WARNING, "couldn't perform action " + e.getMessage(), e);
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                Dict.logger.log(Level.WARNING, "Statement couldn't be closed" + e.getMessage(), e);
            }
        }
        response.responseCode = 200;
        response.data = temp;
        return response;
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
            Dict.logger.log(Level.WARNING, "couldn't perform " + word[0] + " " + word[1] + " " + e.getMessage(), e);
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                Dict.logger.log(Level.WARNING, "Statement couldn't be closed" + e.getMessage(), e);
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
            Dict.logger.log(Level.WARNING, "couldn't perform " + word[0] + " " + word[1] + " " + e.getMessage(), e);
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                Dict.logger.log(Level.WARNING, "Statement couldn't be closed" + e.getMessage(), e);
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
