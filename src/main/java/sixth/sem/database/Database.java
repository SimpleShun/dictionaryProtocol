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
        String temp = "The available database are the following:\n";
        Response response = new Response();

        try {
            statement = connection.prepareStatement("SHOW DATABASES");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(1).equals("information_schema"))
                    continue;
                // temp = temp.concat(resultSet.getString(1) + "\n");
                if (resultSet.getString(1).equals("dictionary")) {
                    temp = temp.concat("en");
                } else if (resultSet.getString(1).equals("french")) {
                    temp = temp.concat("fr");
                }
            }
        } catch (SQLException e) {
            temp = "Failed to perform the action";
            Dict.logger.log(Level.WARNING, "couldn't perform action " + e.getMessage(), e);
        }
        response.responseCode = 200;
        response.data = temp + "\n";
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
        response.data = temp + "\n";
        return response;
    }

    // add {WORD} {DEFINATION}
    public Response add(String[] word) {
        Response response = new Response();
        try {
            if (evaluateShortcut(word[1]).equals("no")) {
                response.data = "No Such Database \n Hint: Show db";
                return response;

            }
            connection.createStatement().execute("use " + evaluateShortcut(word[1]));
            statement = connection.prepareStatement("INSERT INTO " + name_of_table + " VALUES(? , ?)");
            statement.setString(1, word[2]);
            statement.setString(2, rest(word));

            if (statement.executeUpdate() == 1) {
                response.data = word[1] + " was Successfully added";
            } else {
                response.data = "Failed to add " + word[1];
            }

        } catch (SQLException e) {
            response.data = "Failed to peform action";
            Dict.logger.log(Level.WARNING, "couldn't perform " + word[0] + " " + word[1] + " " + e.getMessage(), e);
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                Dict.logger.log(Level.WARNING, "Statement couldn't be closed" + e.getMessage(), e);
            }
        }
        response.data = response.data + "\n";
        return response;
    }

    // REMOVE word
    public Response remove(String[] word) {
        Response response = new Response();
        try {
            if (evaluateShortcut(word[1]).equals("no")) {
                response.data = "No Such Database \n Hint: Show db";
                return response;
            }
            connection.createStatement().execute("use " + evaluateShortcut(word[1]));
            statement = connection.prepareStatement("DELETE FROM " + name_of_table + " WHERE word=?");
            statement.setString(1, word[2]);

            if (statement.executeUpdate() == 1) {
                response.data = word[2] + " was Successfully removed";
            } else {
                response.data = "Failed to remove" + word[2];
            }

        } catch (SQLException e) {
            response.data = "Failed to peform action";
            Dict.logger.log(Level.WARNING, "couldn't perform " + word[0] + " " + e.getMessage(), e);
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                Dict.logger.log(Level.WARNING, "Statement couldn't be closed" + e.getMessage(), e);
            }
        }
        response.data = response.data + "\n";
        return response;
    }

    public Response define2(String[] word) {
        String temp = "";
        Response response = new Response();
        try {
            // statement.execute("use " + word[1]);
            if (evaluateShortcut(word[1]).equals("no")) {
                response.data = "No Such Database \n Hint: Show db";
                return response;

            }
            connection.createStatement().execute("use " + evaluateShortcut(word[1]));
            statement = connection.prepareStatement("SELECT * FROM " + name_of_table + " WHERE word= ?");
            for (int i = 2; i < word.length; i++) {
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
        response.data = temp + "\n";
        return response;
    }

    private String rest(String[] word) {
        String temp = "";
        for (int i = 3; i < word.length; i++) {
            temp = temp.concat(" " + word[i]);
        }
        return temp;
    }

    private String evaluateShortcut(String arg) {
        return switch (arg.trim()) {
            case "en" -> "dictionary";
            default -> "no";
        };
    }
}
