package sixth.sem.dictionary;

import sixth.sem.database.Database;

/**
 * CmdHandler
 */
public class CmdHandler {

    private static Database database;
    private static String[] strArgs;

    private CmdHandler() {
    }

    public static String handle(String cmd, Database db, Again loop) {
        database = db;
        strArgs = cmd.toLowerCase().split(" ");

        return switch (strArgs[0]) {
            case "help" -> help.apply();
            case "quit" -> quit(loop);
            case "define" -> define.apply();
            case "add" -> add.apply();
            case "remove" -> remove.apply();

            default -> "Wrong Cmd String , Use HELP cmd for more info";
        };
    }

    private static Fn help = () -> {
        if (strArgs.length != 1) {
            return "Only Help";
        }
        return """
                Usuage:
                +--------------------------------------------+
                + Commands| args          |      remarks     +
                +---------|---------------|------------------+
                + ADD     | word [word..] | Adds to db       +
                +---------|---------------|------------------+
                + REMOVE  |    word       | removes from db  +
                +---------|---------------|------------------+
                + DEFINE  |    word       | defines the word +
                +---------|---------------|------------------+
                + QUIT    |               |  Close Connction +
                +--------------------------------------------+
                """;
    };

    private static String quit(Again l) {
        if (strArgs.length != 1) {
            return "Only Quit";
        }
        // database.stop();
        // Dict.loop = false;
        l.loop = false;
        return "221 Closing Connection";
    }

    private static Fn define = () -> {
        if (strArgs.length == 1) {
            return "Not Enough Args";
        }
        return database.define(strArgs);
    };

    private static Fn add = () -> {
        if (strArgs.length == 1) {
            return "Not Enough Args";
        }
        return database.add(strArgs);
    };

    private static Fn remove = () -> {
        if (strArgs.length == 1) {
            return "Not Enough Args";
        }
        return database.remove(strArgs);
    };

}

/**
 * InnerCmdHandler
 */
interface Fn {
    public String apply();
}
// - [ ] DEFINE database word [ DEFINE WORD ]
// - [ ] MATCH database stragety word
// - [ ] SHOW db | SHOW databases
// - [ ] SHOW strat | SHOW starageties
// - [ ] SHOW INFO database
// - [ ] SHOW server
// - [ ] CLIENT text
// - [ ] STATUS
// - [ ] HELP
// - [x] QUIT
// - [ ] OPTION mime
