package org.example.db.beans;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbutils.BeanProcessor;
import org.example.db.db_utils.DB_Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Setter
@Getter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Language {

    Integer language_id;
    String name;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("language_id", "name", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Language> getAllFromLanguage() throws SQLException {
        String query = "SELECT * FROM language;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Language.class);
        }
    }

    public static Language getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM language WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Language.class);
        }
    }

    public static Language insert(Language language) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO language (name, last_update) VALUES (?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                language.getName(),
                language.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("language_id", newId);
            }
            return null;
        }
    }

    public static Language update(int language_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE language SET " + column + " = ? WHERE language_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, language_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("language_id", language_id);
    }

    public static Language delete(int language_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Language languageToDelete = getBy("language_id", language_id);
        String query = "DELETE FROM language WHERE language_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, language_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return languageToDelete;
    }
}
