package sixth.sem.fileHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        final var configPath = FileSystems.getDefault().getPath("dictionary.conf");

        if (!Files.exists(configPath)) {
            Dict.logger.info("dictionary.conf doesn't exists, Creating default config");
            try (var writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8);) {
                writer.write(default_value);
            } catch (IOException e) {
                Dict.logger.log(Level.SEVERE, "Operation Failed " + e.getMessage(), e);
                System.exit(-1);
            }
        }

        try (var file_reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
            return file_reader.lines()
                    .parallel()
                    .filter(Help.filter)
                    .map(String::toLowerCase)
                    .collect(Collector.of(
                            HashMap::new,
                            Help.accumulator,
                            Help.combiner,
                            (m) -> Collections.unmodifiableMap(m)));

        } catch (IOException e) {
            Dict.logger.log(Level.SEVERE, "Operation Failed " + e.getMessage(), e);
            System.exit(-1);
            return new HashMap<>();
        }
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
        return s.isEmpty()
                ? false
                : s.charAt(0) == '#'
                        ? false
                        : s.contains("=");
    };
}
