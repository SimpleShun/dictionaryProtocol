package sixth.sem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Stream;

import sixth.sem.Response;
import sixth.sem.dictionary.Dict;

public final class Database {
    private final String name_of_table;
    private Connection connection;

    public Database(String databaseAddr, String username, String password, String tablename) {
        name_of_table = tablename;
        try {
            connection = DriverManager.getConnection(databaseAddr, username, password);
            Dict.logger.info("Connected Successfully to Database on " + databaseAddr);
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
        try (var statement = connection.prepareStatement("SHOW DATABASES");) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(1).equals("information_schema"))
                    continue;
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
        response.data = temp;
        return response;
    }

    public Response define_stream(String[] words) {
        var response = new Response();
        response.data = Stream.of(words).map((word) -> {
            String temp = "";
            try (var statement = connection.prepareStatement("SELECT * FROM " + name_of_table + " WHERE word= ?");) {
                statement.setString(1, word);
                var result = statement.executeQuery();
                while (result.next()) {
                    if (result.getString("value").isEmpty())
                        temp = temp.concat(word + "-> " + "not in the Database\n");
                    temp = temp.concat(word + " -> " + result.getString("value") + "\n");
                }
                return temp;
            } catch (SQLException e) {
                temp = "Failed to perform the action";
                Dict.logger.log(Level.WARNING, "couldn't perform action " + e.getMessage(), e);
                return temp;
            }
        }).toString();
        response.responseCode = 200;
        return response;
    }

    public Response define(String[] word) {
        String temp = "";
        Response response = new Response();
        try (var statement = connection.prepareStatement("SELECT * FROM " + name_of_table + " WHERE word= ?");) {
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
        }
        response.responseCode = 200;
        response.data = temp;
        return response;
    }

    public Response add(String[] word) {
        Response response = new Response();
        if (evaluateShortcut(word[1]).equals("no")) {
            response.data = "No Such Database \n Hint: Show db";
            return response;
        }

        try (var statement = connection.prepareStatement("INSERT INTO " + name_of_table + " VALUES(? , ?)");) {
            connection.createStatement().execute("use " + evaluateShortcut(word[1]));
            statement.setString(1, word[2]);
            statement.setString(2, rest(word));

            response.data = statement.executeUpdate() == 1
                    ? word[1] + " was Successfully added"
                    : "Failed to add " + word[1];
        } catch (SQLException e) {
            response.data = "Failed to peform action";
            Dict.logger.log(Level.WARNING, "couldn't perform " + word[0] + " " + word[1] + " " + e.getMessage(), e);
        }
        return response;
    }

    public Response remove(String[] word) {
        Response response = new Response();
        if (evaluateShortcut(word[1]).equals("no")) {
            response.data = "No Such Database \n Hint: Show db";
            return response;
        }
        try (var statement = connection.prepareStatement("DELETE FROM " + name_of_table + " WHERE word=?");) {
            connection.createStatement().execute("use " + evaluateShortcut(word[1]));
            statement.setString(1, word[2]);

            response.data = statement.executeUpdate() == 1
                    ? word[2] + " was Successfully removed"
                    : "Failed to remove" + word[2];
        } catch (SQLException e) {
            response.data = "Failed to peform action";
            Dict.logger.log(Level.WARNING, "couldn't perform " + word[0] + " " + e.getMessage(), e);
        }
        return response;
    }

    public Response match(String[] word) {
        String temp = "";
        Response response = new Response();
        String sqlQuery = "SELECT * FROM " + name_of_table + " WHERE word REGEXP ";
        switch (word[1]) {
            case "prefix":
                sqlQuery = sqlQuery + "\"^" + word[3] + "\"";
                break;
            case "suffix":
                sqlQuery = sqlQuery + "\"" + word[3] + "$\"";
                break;
            case "exact":
                sqlQuery = sqlQuery + word[3];
                break;
            default:
                sqlQuery.concat(word[3]);
                break;

        }
        try (var statement = connection.createStatement();) {
            // use the preferred database
            connection.createStatement().execute("use " + evaluateShortcut(word[2]));

            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next()) {
                temp = temp.concat(result.getString("word") + " -> " + result.getString("value"));
            }
        } catch (SQLException e) {
            temp = "Failed to perform the action";
            Dict.logger.log(Level.WARNING, "couldn't perform action " + e.getMessage(), e);
        }
        response.responseCode = 200;
        response.data = temp;
        return response;
    }

    public Response define2(String[] word) {
        String temp = "";
        Response response = new Response();
        if (evaluateShortcut(word[1]).equals("no")) {
            response.data = "No Such Database \n Hint: Show db";
            return response;
        }
        try (var statement = connection.prepareStatement("SELECT * FROM " + name_of_table + " WHERE word= ?");) {
            connection.createStatement().execute("use " + evaluateShortcut(word[1]));
            for (int i = 2; i < word.length; i++) {
                statement.setString(1, word[i]);
                ResultSet result = statement.executeQuery();
                int loops_to_help_know_if_resultset_is_empty = 0;
                while (result.next()) {
                    ++loops_to_help_know_if_resultset_is_empty;
                    temp = temp.concat(word[i] + " -> " + result.getString("value"));
                }
                if (loops_to_help_know_if_resultset_is_empty == 0) {
                    temp = temp.concat(word[i] + "-> " + "not in the Database");
                }
            }
        } catch (SQLException e) {
            temp = "Failed to perform the action";
            Dict.logger.log(Level.WARNING, "couldn't perform action " + e.getMessage(), e);
        }
        response.responseCode = 200;
        response.data = temp;
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
