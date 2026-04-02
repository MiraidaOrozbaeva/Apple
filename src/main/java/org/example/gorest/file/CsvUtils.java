package org.example.gorest.file;

import org.example.gorest.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CsvUtils {

    private static final String CSV_HEADER = "id,name,email,gender,status";
    private static final String DELIMITER = ",";
    private static final String NEW_LINE = "\n";
    private static final Logger log = LoggerFactory.getLogger(CsvUtils.class);

    private CsvUtils() {
        // utility class
    }

    public static void writeUsersToCsv(List<User> users, String filePath) {
        createDirectories(filePath);

        try (FileWriter writer = new FileWriter(filePath)) {

            writer.append(CSV_HEADER).append(NEW_LINE);

            for (User user : users) {
                writer.append(String.valueOf(user.getId())).append(DELIMITER)
                        .append(user.getName()).append(DELIMITER)
                        .append(user.getEmail()).append(DELIMITER)
                        .append(user.getGender()).append(DELIMITER)
                        .append(user.getStatus()).append(NEW_LINE);
            }

            log.info("CSV file successfully created: {}", filePath);

        } catch (IOException e) {
            log.error("Failed to write CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to write CSV file", e);
        }
    }

    private static void createDirectories(String filePath) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
        } catch (IOException e) {
            log.error("Failed to create directories for path: {}", filePath, e);
            throw new RuntimeException("Failed to create directories", e);
        }
    }

    public static List<User> readUsersFromCsv(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<User> users = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) { // i=1 пропускаем header
                String[] columns = lines.get(i).split(DELIMITER);
                User user = User.builder()
                        .id(Integer.valueOf(columns[0]))
                        .name(columns[1])
                        .email(columns[2])
                        .gender(columns[3])
                        .status(columns[4])
                        .build();
                users.add(user);
            }

            log.info("CSV file successfully read: {}, total users: {}", filePath, users.size());
            return users;

        } catch (IOException e) {
            log.error("Failed to read CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    public static User getRandomUserFromCsv(String filePath) {
        List<User> users = readUsersFromCsv(filePath);
        int randomIndex = new Random().nextInt(users.size());
        User randomUser = users.get(randomIndex);
        log.info("Random user selected: {}", randomUser);
        return randomUser;
    }
}
