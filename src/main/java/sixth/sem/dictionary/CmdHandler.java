package sixth.sem.dictionary;

import sixth.sem.App;
import sixth.sem.Response;
import sixth.sem.database.Database;

/**
 * CmdHandler
 */
public class CmdHandler {

    private static Database database;
    private static String[] strArgs;
    private static Auth authc;

    private CmdHandler() {
    }

    public static String handle(String cmd, Database db, Again loop, Auth auth) {
        database = db;
        authc = auth;
        strArgs = cmd.toLowerCase().split(" ");

        return switch (strArgs[0]) {
            case "help" -> help.apply();
            case "quit" -> quit(loop);
            // case "define" -> define.apply();
            case "define" -> define2.apply();
            case "add" -> add.apply();
            case "remove" -> remove.apply();
            case "abort" -> abort.apply();
            case "show" -> show.apply();
            case "auth" -> auth(strArgs, auth).data;

            default -> "Wrong Cmd String , Use HELP cmd for more info";
        };
    }

    private static Fn<String> help = () -> {
        if (strArgs.length != 1) {
            return "Only Help";
        }
        return """
                Usuage:
                    add {dictionary} {word}
                    remove {database} {word}
                    show db || show database
                    help
                    quit
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

    // private static Fn<String> define = () -> {
    // if (strArgs.length == 0) {
    // return "Not Enough Args";
    // }
    // return database.define(strArgs).data;
    // };

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
        if (strArgs[1].equals("db") || strArgs[1].equals("databases"))
            return database.showDb(strArgs).data;
        else
            return "Wrong Command";
    };

    private static Response auth(String[] args, Auth auth) {
        Response response = new Response();
        if (args.length != 3) {
            response.data = "wrong statement";
            return response;
        }
        if (args[1].equals(App.getUsername()) && args[2].equals(App.getPassword())) {
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
