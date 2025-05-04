package sixth.sem.fileHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sixth.sem.dictionary.Dict;

/**
 * ReadConfigV2
 */
public final class ReadConfigV2 {
    private static final String default_value = """
            databaseAddr=jdbc:mariadb://localhost:3306/dictionary
            username=dict
            password=123
            name_of_table=dictionary
            port=2628
                   """;

    private ReadConfigV2() {
    }

    public static Map<String, String> setup() {
        Map<String, String> map = new HashMap<>();
        final var configPath = FileSystems.getDefault().getPath("dictionary.conf");

        if (!Files.exists(configPath)) {
            Dict.logger.info("dictionary.conf doesn't exists, Creating default config file");
            try {
                Files.createFile(configPath);
                try (var fileWriter = new FileWriter(configPath.toFile(), true)) {
                    fileWriter.write(default_value);
                    Dict.logger.info("dictionary.conf created with default values");
                }
            } catch (IOException e) {
                Dict.logger.log(Level.SEVERE, "Operation Failed " + e.getMessage(), e);
                System.exit(-1);
            }
        }
        try (var file_data = Files.newBufferedReader(configPath)) {
            map = file_data.lines().parallel()
                    .filter(Predicate.not(Help.filter))
                    .map((s) -> s.toLowerCase())
                    .collect(Collector.of(
                            HashMap::new,
                            Help.accumulator,
                            Help.combiner,
                            (m) -> Collections.unmodifiableMap(m)));
        } catch (Exception e) {
            Dict.logger.log(Level.SEVERE, "Operation Failed " + e.getMessage(), e);
            System.exit(-1);
        }
        return map;
    }
}

final class Help {
    private Help() {
    }

    static BiConsumer<Map<String, String>, String> accumulator = (container, in) -> {
        String a[] = in.trim().split("=");
        container.put(a[0].trim(), a[1].trim());
    };

    static BinaryOperator<Map<String, String>> combiner = (m1, m2) -> {
        return Stream.concat(m1.entrySet().stream(), m2.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    };
    static Predicate<String> filter = (s) -> {
        return s.isEmpty() ? true : s.charAt(0) == '#';
    };
}
