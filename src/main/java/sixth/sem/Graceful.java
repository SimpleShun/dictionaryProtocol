package sixth.sem;

import java.lang.Runtime;

import sixth.sem.dictionary.Dict;

/**
 * Graceful
 */
public class Graceful {
    Graceful() {
        var thread_task = Thread.ofPlatform().unstarted(() -> {
            Dict.logger.info("Starting Graceful Shutdown");
        });
        Runtime.getRuntime().addShutdownHook(thread_task);
    }

}
