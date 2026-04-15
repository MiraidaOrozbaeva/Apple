package org.example.db.beans;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbutils.BeanProcessor;
import org.example.db.db_utils.DatabaseConnection;

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

public class Inventory {

    Integer inventory_id;
    Integer film_id;
    Integer store_id;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("inventory_id", "film_id", "store_id", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Inventory> getAllFromInventory() throws SQLException {
        String query = "SELECT * FROM inventory;";
        try(ResultSet resultSet = DatabaseConnection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Inventory.class);
        }
    }

    public static Inventory getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM inventory WHERE " + column + " = ?; ";
        ResultSet resultSet = DatabaseConnection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Inventory.class);
        }
    }

    public static Inventory insert(Inventory inventory) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO inventory (film_id, store_id, last_update) VALUES (?, ?, ?)";
        try (ResultSet generatedKeys = DatabaseConnection.makeInsert(query,
                inventory.getFilm_id(),
                inventory.getStore_id(),
                inventory.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("inventory_id", newId);
            }
            return null;
        }
    }

    public static Inventory update(int inventory_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE inventory SET " + column + " = ? WHERE inventory_id = ?";
        int affectedRows = DatabaseConnection.makeUpdate(query, newValue, inventory_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("inventory_id", inventory_id);
    }

    public static Inventory delete(int inventory_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Inventory inventoryToDelete = getBy("inventory_id", inventory_id);
        String query = "DELETE FROM inventory WHERE inventory_id = ?";
        int affectedRows = DatabaseConnection.makeUpdate(query, inventory_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return inventoryToDelete;
    }
}
