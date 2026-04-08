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

public class Film_category {

    Integer film_id;
    Integer category_id;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("film_id", "category_id", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Film_category> getAllFromFilmCategory() throws SQLException {
        String query = "SELECT * FROM film_category;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Film_category.class);
        }
    }

    public static Film_category getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM film_category WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Film_category.class);
        }
    }

    public static Film_category insert(Film_category filmCategory) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO film_category (category_id, last_update) VALUES (?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                filmCategory.getCategory_id(),
                filmCategory.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("film_id", newId);
            }
            return null;
        }
    }

    public static Film_category update(int film_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE film_category SET " + column + " = ? WHERE film_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, film_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("film_id", film_id);
    }

    public static Film_category delete(int film_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Film_category filmCategoryToDelete = getBy("film_id", film_id);
        String query = "DELETE FROM film_category WHERE film_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, film_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return filmCategoryToDelete;
    }
}
