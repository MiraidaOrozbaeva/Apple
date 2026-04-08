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

public class Store {

    Integer store_id;
    Integer manager_staff_id;
    Integer address_id;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("store_id", "manager_staff_id", "address_id", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Store> getAllFromStore() throws SQLException {
        String query = "SELECT * FROM store;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Store.class);
        }
    }

    public static Store getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM store WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Store.class);
        }
    }

    public static Store insert(Store store) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO store (manager_staff_id, address_id, last_update) VALUES (?, ?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                store.getManager_staff_id(),
                store.getAddress_id(),
                store.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("store_id", newId);
            }
            return null;
        }
    }

    public static Store update(int store_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE store SET " + column + " = ? WHERE store_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, store_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("store_id", store_id);
    }

    public static Store delete(int store_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Store storeToDelete = getBy("store_id", store_id);
        String query = "DELETE FROM store WHERE store_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, store_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return storeToDelete;
    }
}
