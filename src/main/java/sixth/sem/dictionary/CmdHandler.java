package sixth.sem.dictionary;

import sixth.sem.database.Database;

/**
 * CmdHandler
 */
public class CmdHandler {
    Database database = new Database();

    public static String handle(String cmd) {
        return switch (cmd.toLowerCase()) {
            case "help" -> help.apply();

            default -> "Wrong Cmd String , Use HELP cmd for more info";
        };
    }

    // void handle(String cmd) {
    // String[] words = cmd.split(" ");
    // switch (words[0]) {
    // case "DEFINE":
    // case "define":
    // if (words.length != 2) {
    // System.err.println("Usuage: DEFINE word");
    // System.exit(-1);
    // }
    // database.define(words[1]);
    // break;
    // case "ADD":
    // case "add":
    // if (words.length != 2) {
    // System.err.println("Usuage: DEFINE word");
    // System.exit(-1);
    // }
    // database.define(words[1]);
    // break;
    //
    // case "HELP":
    // case "help":
    // help.apply();
    // break;
    // default:
    // break;
    // }
    // }

    private static Fn help = () -> {
        return """
                Usuage: App DEFINE word
                            - to define the word
                        App ADD word "define here"
                            - to add a word to the database
                    """;
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
