package sixth.sem.fileHandler;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;

import sixth.sem.App;
import sixth.sem.dictionary.Dict;

/**
 * ReadConfig
 */
public class ReadConfig {
    // private ReadConfig readConfig = new ReadConfig();
    private static FileWriter fileWriter;
    private static Path configPath;
    private static Scanner fileScanner;

    private static String defautlValues = """
            databaseAddr=jdbc:mariadb://localhost:3306/dictionary
            username=dict
            password=123
            name_of_table=dictionary
            port=2628
                   """;

    private ReadConfig() {
    }

    public static void canPopulate() {
        configPath = FileSystems.getDefault().getPath("dictionary.conf");

        if (!Files.exists(configPath)) {
            Dict.logger.info("dictionary.conf doesn't exists, Creating default config file");
            try {
                Files.createFile(configPath);
                fileWriter = new FileWriter(configPath.toFile());
                fileWriter.write(defautlValues);
                fileWriter.flush();
                Dict.logger.info("dictionary.conf created with default values");
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                Dict.logger.log(Level.SEVERE, "Operation Failed " + e.getMessage(), e);
            }
        }
        setValues();

    }

    private static void setValues() {
        try {
            fileScanner = new Scanner(configPath.toFile());

            String temp;
            while (fileScanner.hasNextLine()) {
                temp = fileScanner.nextLine();
                if (temp.isEmpty())
                    continue;
                if (!temp.contains("=")) {
                    Dict.logger.log(Level.SEVERE, "Not a key value pair");
                    Dict.logger.log(Level.SEVERE, "Aborting");
                    System.exit(-1);
                }
                performAction(temp.trim().split("="));
            }

        } catch (FileNotFoundException e) {
            Dict.logger.log(Level.SEVERE, "Config file not present " + e.getMessage(), e);
            Dict.logger.log(Level.SEVERE, "Aborting");
            System.exit(-1);
        }
        if (fileScanner != null) {
            fileScanner.close();
        }
    }

    private static void performAction(String[] args) {
        if (args.length != 2) {
            Dict.logger.log(Level.SEVERE, args[0] + "must have only 1 value");
            Dict.logger.log(Level.SEVERE, "Aborting");
            System.exit(-1);
        }
        switch (args[0].trim()) {
            case "databaseAddr":
                App.setDatabaseAddr(args[1].trim());
                break;
            case "username":
                App.setUsername(args[1].trim());
                break;
            case "password":
                App.setPassword(args[1].trim());
                break;
            case "name_of_table":
                App.setName_of_table(args[1].trim());
                break;
            case "port":
                int port = -1;
                try {
                    port = Integer.parseInt(args[1].trim());
                } catch (NumberFormatException e) {
                    Dict.logger.log(Level.SEVERE, args[0].trim() + " must be a valid port number");
                    Dict.logger.log(Level.SEVERE, "Aborting");
                    System.exit(-1);
                }
                App.setPort(port);
                break;
            default:
                Dict.logger.log(Level.SEVERE, args[0].trim() + " is not valid field");
                Dict.logger.log(Level.SEVERE, "Aborting");
                System.exit(-1);
        }
        ;
    }

}
