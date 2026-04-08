package org.example.db.beans;

import kg.example.db.db_utils.DB_Connection;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbutils.BeanProcessor;

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

public class Category {

    Integer category_id;
    String name;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("category_id", "name", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Category> getAllFromCategory() throws SQLException {
        String query = "SELECT * FROM category;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Category.class);
        }
    }

    public static Category getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM category WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Category.class);
        }
    }

    public static Category insert(Category category) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO category (name, last_update) VALUES (?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                category.getName(),
                category.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("category_id", newId);
            }
            return null;
        }
    }

    public static Category update(int category_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE category SET " + column + " = ? WHERE category_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, category_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("category_id", category_id);
    }

    public static Category delete(int category_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Category categoryToDelete = getBy("category_id", category_id);
        String query = "DELETE FROM category WHERE category_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, category_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return categoryToDelete;
    }
}
