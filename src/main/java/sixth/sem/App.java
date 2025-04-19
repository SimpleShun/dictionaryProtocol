package sixth.sem;

import sixth.sem.dictionary.Dict;

/**
 * Hello world!
 */
public class App {
    private static int port = 2628;

    public static void main(String[] args) {
        new Dict(port);
    }
}
