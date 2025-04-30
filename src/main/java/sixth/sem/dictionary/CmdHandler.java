package sixth.sem.dictionary;

import sixth.sem.App;
import sixth.sem.Response;
import sixth.sem.database.Database;
import sixth.sem.dictionary.Dict;

/**
 * CmdHandler
 */
public class CmdHandler {

    private static Database database;
    private static String[] strArgs;
    private static Auth authc;

    private CmdHandler() {
    }

    public static Response handle(String cmd, Database db, Again loop, Auth auth) {
        database = db;
        authc = auth;
        strArgs = cmd.toLowerCase().split(" ");

        return switch (strArgs[0]) {
            case "help" -> help.apply();
            case "quit" -> quit(loop);
            case "define" -> define2.apply();
            case "add" -> add.apply();
            case "remove" -> remove.apply();
            case "abort" -> abort.apply();
            case "show" -> show.apply();
            case "auth" -> auth(strArgs, auth);
            default -> new Response("Wrong Cmd String , Use HELP cmd for more info", 200);
        };
    }

    private static Fn<Response> help = () -> {
        if (strArgs.length != 1) {
            return new Response("Only Help", 400);
        }
        var Str = """
                Usuage:
                    add {dictionary} {word}
                    remove {database} {word}
                    show db || show database
                    help
                    quit
                """;
        return new Response(Str, 200);
    };

    private static Response quit(Again l) {
        if (strArgs.length != 1) {
            return new Response("Only Quit", 200);
        }
        l.loop = false;
        return new Response("221 Closing Connection", 200);
    }

    // private static Fn<String> define = () -> {
    // if (strArgs.length == 0) {
    // return "Not Enough Args";
    // }
    // return database.define(strArgs).data;
    // };

    private static Fn<Response> define2 = () -> {
        if (strArgs.length == 1) {
            return new Response("Not Enough Args", 200);
        }
        return database.define2(strArgs);
    };

    private static Fn<Response> add = () -> {
        if (strArgs.length == 1) {
            return new Response("Not Enough Args", 200);
        }
        if (!authc.getAuth()) {
            return new Response("Not Authenticated", 200);
        }
        return database.add(strArgs);
    };

    private static Fn<Response> remove = () -> {
        if (strArgs.length == 1) {
            return new Response("Not Enough Args", 200);
        }
        if (!authc.getAuth()) {
            return new Response("Not Authenticated", 200);
        }
        return database.remove(strArgs);
    };

    private static Fn<Response> abort = () -> {
        if (strArgs.length != 1) {
            return new Response("Only Abort", 200);
        }
        if (!authc.getAuth()) {
            return new Response("Not Authenticated", 200);
        }
        Dict.terminate();
        return new Response("Done", 200);
    };

    private static Fn<Response> show = () -> {
        if (strArgs.length == 1) {
            return new Response("Wrong Command", 200);
        }
        if (strArgs[1].equals("db") || strArgs[1].equals("databases"))
            return database.showDb(strArgs);
        else
            return new Response("Wrong Command", 200);
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
