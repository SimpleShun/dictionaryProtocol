package sixth.sem.dictionary;

import sixth.sem.database.Database;

/**
 * CmdHandler
 */
public class CmdHandler {

    private static Database database;
    private static String[] strArgs;

    public static String handle(String cmd, Database db, Again loop) {
        database = db;
        strArgs = cmd.toLowerCase().split(" ");

        return switch (strArgs[0]) {
            case "help" -> help.apply();
            case "quit" -> quit(loop);
            case "define" -> database.define(strArgs);
            case "add" -> database.add(strArgs);
            case "remove" -> database.remove(strArgs);

            default -> "Wrong Cmd String , Use HELP cmd for more info";
        };
    }

    private static Fn help = () -> {
        if (strArgs.length != 1) {
            return "Only Help";
        }
        // return """
        // Usuage:
        // define word or
        // define word word word ....
        // - to define the word
        //
        // ADD word define
        // - to add a word to the database
        // """;

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
                + QUIT    |               |  closes server   +
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

    // private static Fn quit = () -> {
    // if (strArgs.length != 1) {
    // return "Only Quit";
    // }
    // // database.stop();
    // // Dict.loop = false;
    //
    // return "221 Closing Connection";
    // };

    // private static Fn define = () -> {
    // return database.define(strArgs);
    // };
    //
    // private static Fn add = () -> {
    // return database.add(strArgs);
    // };

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
