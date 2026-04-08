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

public class Rental {

    Integer rental_id;
    Timestamp rental_date;
    Integer inventory_id;
    Integer customer_id;
    Timestamp return_date;
    Integer staff_id;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("rental_id", "rental_date", "inventory_id", "customer_id", "return_date",
                "staff_id", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Rental> getAllFromRental() throws SQLException {
        String query = "SELECT * FROM rental;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Rental.class);
        }
    }

    public static Rental getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM rental WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Rental.class);
        }
    }

    public static Rental insert(Rental rental) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO rental (rental_date, inventory_id, customer_id, return_date, " +
                "staff_id, last_update) VALUES (?, ?, ?, ?, ?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                rental.getRental_date(),
                rental.getInventory_id(),
                rental.getCustomer_id(),
                rental.getReturn_date(),
                rental.getStaff_id(),
                rental.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("rental_id", newId);
            }
            return null;
        }
    }

    public static Rental update(int rental_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE rental SET " + column + " = ? WHERE rental_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, rental_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("rental_id", rental_id);
    }

    public static Rental delete(int rental_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Rental rentalToDelete = getBy("rental_id", rental_id);
        String query = "DELETE FROM rental WHERE rental_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, rental_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return rentalToDelete;
    }
}
