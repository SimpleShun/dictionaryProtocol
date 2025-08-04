package sixth.sem.dictionary;

import sixth.sem.App;
import sixth.sem.Response;
import sixth.sem.database.Database;

public class CmdHandler {
    private static String[] strArgs;
    private static String[] forEnc;
    private static Database database;
    private static Auth authc;

    private CmdHandler() {
    }

    public static String handle(final String cmd, final Database db, final Again loop, final Auth auth) {
        authc = auth;
        database = db;
        forEnc = cmd.split(" ");
        strArgs = cmd.toLowerCase().split(" ");
        return switch (strArgs[0]) {
            case "help" -> help.apply();
            case "quit" -> quit(loop);
            case "define" -> define2.apply();
            case "add" -> add.apply();
            case "remove" -> remove.apply();
            case "abort" -> abort.apply();
            case "show" -> show.apply();
            case "match" -> match.apply();
            case "auth" -> auth(forEnc, auth).data;
            default -> "Wrong Cmd String , Use HELP cmd for more info";
        };
    }

    private static Fn<String> help = () -> {
        if (strArgs.length != 1) {
            return "Only Help";
        }
        return """
                Usuage:
                    DEFINE dictionary word      -- Look up words in the database
                    MATCH strat dictionary word -- Look up words in the database
                    SHOW strat                  -- List Stragety
                    ADD dictionary word define  -- Add words to the database
                    REMOVE database word        -- Remove words from the database
                    SHOW db                     -- List all the available database
                    SHOW database               -- List all the available database
                    AUTH user password          -- Provide authentication information
                    HELP                        -- Display the help information
                    QUIT                        -- Terminate connection
                """;
    };

    private static String quit(Again l) {
        if (strArgs.length != 1) {
            return "Only Quit";
        }
        l.loop = false;
        return "Closing Connection";
    }

    private static Fn<String> define2 = () -> {
        if (strArgs.length == 1) {
            return "Not Enough Args";
        }
        return database.define2(strArgs).data;
    };

    private static Fn<String> add = () -> {
        if (strArgs.length == 1) {
            return "Not Enough Args";
        }
        if (!authc.getAuth()) {
            return "Not Authenticated";
        }
        return database.add(strArgs).data;
    };

    private static Fn<String> remove = () -> {
        if (strArgs.length == 1) {
            return "Not Enough Args";
        }
        if (!authc.getAuth()) {
            return "Not Authenticated";
        }
        return database.remove(strArgs).data;
    };

    private static Fn<String> abort = () -> {
        if (strArgs.length != 1) {
            return "Only Abort";
        }
        if (!authc.getAuth()) {
            return "Not Authenticated";
        }
        Dict.terminate();
        return "Done";
    };

    private static Fn<String> show = () -> {
        if (strArgs.length == 1) {
            return "Wrong Command";
        }
        if (strArgs[1].equals("db") || strArgs[1].equals("database"))
            return database.showDb(strArgs).data;
        else if (strArgs[1].equals("stragety") || strArgs[1].equals("strat"))
            return """
                    3 Strageties present
                     prefix "Match prefixes"
                     suffix "Match suffixes"
                     """;
        // exact "Match words exactly"
        else
            return "Wrong Command";
    };

    private static Fn<String> match = () -> {
        if (strArgs.length != 4)
            return "Wrong Command";
        else
            return database.match(strArgs).data;
    };

    private static Response auth(String[] args, Auth auth) {
        Response response = new Response();
        if (args.length != 3) {
            response.data = "wrong statement";
            return response;
        }

        var String = Encrypt.decrypt(args[2]);
        if (args[1].equals(App.getUsername()) && String.equals(App.getPassword())) {
            response.data = "Authenticated";
            auth.setAuth(true);
            return response;
        } else {
            response.data = "Not Authenticated";
            return response;
        }
    }
}

/**
 * InnerCmdHandler
 */
interface Fn<t> {
    public t apply();
}
