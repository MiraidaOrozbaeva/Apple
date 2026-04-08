package org.example.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvUtils {

    private static final String DELIMITER = ",";
    private static final String NEW_LINE = "\n";
    private static final Logger log = LoggerFactory.getLogger(CsvUtils.class);

    private CsvUtils() {
        // utility class
    }

    // ========================= WRITE =========================

    /**
     * Универсальный метод записи любого списка объектов в CSV
     * Колонки берутся автоматически через рефлексию из полей класса
     *
     * Пример: CsvUtils.writeToCsv(actors, "target/csv/actors.csv", Actor.class);
     */
    public static <T> void writeToCsv(List<T> items, String filePath, Class<T> clazz) {
        createDirectories(filePath);
        Field[] fields = clazz.getDeclaredFields();

        try (FileWriter writer = new FileWriter(filePath)) {

            // HEADER — берём названия полей класса
            List<String> headers = new ArrayList<>();
            for (Field field : fields) {
                headers.add(field.getName());
            }
            writer.append(String.join(DELIMITER, headers)).append(NEW_LINE);

            // DATA
            for (T item : items) {
                List<String> values = new ArrayList<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(item);
                    values.add(value != null ? value.toString() : "");
                }
                writer.append(String.join(DELIMITER, values)).append(NEW_LINE);
            }

            log.info("CSV file successfully created: {} | rows: {}", filePath, items.size());

        } catch (IOException | IllegalAccessException e) {
            log.error("Failed to write CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to write CSV file", e);
        }
    }

    // ========================= READ =========================

    /**
     * Читает все строки из CSV и возвращает список строк (каждая строка — это Map колонка:значение)
     * Пример: List<Map<String, String>> rows = CsvUtils.readFromCsv("target/csv/actors.csv");
     */
    public static List<Map<String, String>> readFromCsv(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<Map<String, String>> result = new ArrayList<>();

            if (lines.isEmpty()) return result;

            String[] headers = lines.get(0).split(DELIMITER);

            for (int i = 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(DELIMITER, -1);
                Map<String, String> row = new LinkedHashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    row.put(headers[j], j < values.length ? values[j] : "");
                }
                result.add(row);
            }

            log.info("CSV file successfully read: {} | rows: {}", filePath, result.size());
            return result;

        } catch (IOException e) {
            log.error("Failed to read CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    /**
     * Возвращает все значения конкретной колонки
     * Пример: List<String> ids = CsvUtils.getColumn("target/csv/actors.csv", "actor_id");
     */
    public static List<String> getColumn(String filePath, String columnName) {
        List<Map<String, String>> rows = readFromCsv(filePath);
        List<String> values = new ArrayList<>();
        for (Map<String, String> row : rows) {
            values.add(row.get(columnName));
        }
        log.info("Column '{}' extracted | values: {}", columnName, values);
        return values;
    }

    /**
     * Возвращает рандомное значение из конкретной колонки
     * Пример: String randomId = CsvUtils.getRandomValue("target/csv/actors.csv", "actor_id");
     */
    public static String getRandomValue(String filePath, String columnName) {
        List<String> values = getColumn(filePath, columnName);
        String randomValue = values.get(new Random().nextInt(values.size()));
        log.info("Random value from column '{}': {}", columnName, randomValue);
        return randomValue;
    }

    /**
     * Возвращает рандомную строку целиком
     * Пример: Map<String, String> randomRow = CsvUtils.getRandomRow("target/csv/actors.csv");
     */
    public static Map<String, String> getRandomRow(String filePath) {
        List<Map<String, String>> rows = readFromCsv(filePath);
        Map<String, String> randomRow = rows.get(new Random().nextInt(rows.size()));
        log.info("Random row selected: {}", randomRow);
        return randomRow;
    }

    // ========================= PRIVATE =========================

    private static void createDirectories(String filePath) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
        } catch (IOException e) {
            log.error("Failed to create directories for path: {}", filePath, e);
            throw new RuntimeException("Failed to create directories", e);
        }
    }
}