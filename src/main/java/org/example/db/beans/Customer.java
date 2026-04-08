package org.example.db.beans;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbutils.BeanProcessor;
import org.example.db.db_utils.DB_Connection;

import java.sql.Date;
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

public class Customer {

    Integer customer_id;
    Integer store_id;
    String first_name;
    String last_name;
    String email;
    Integer address_id;
    Boolean activebool;
    Date create_date;
    Timestamp last_update;
    Integer active;


    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("customer_id", "store_id", "first_name", "last_name", "email",
                "address_id", "activebool", "create_date", "last_update", "active");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Customer> getAllFromCustomer() throws SQLException {
        String query = "SELECT * FROM customer;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Customer.class);
        }
    }

    public static Customer getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM customer WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Customer.class);
        }
    }

    public static Customer insert(Customer customer) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO customer (store_id, first_name, last_name, email, address_id, " +
                "activebool, create_date, last_update, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                customer.getStore_id(),
                customer.getFirst_name(),
                customer.getLast_name(),
                customer.getEmail(),
                customer.getAddress_id(),
                customer.getActivebool(),
                customer.getCreate_date(),
                customer.getLast_update(),
                customer.getActive())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("customer_id", newId);
            }
            return null;
        }
    }

    public static Customer update(int customer_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE customer SET " + column + " = ? WHERE customer_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, customer_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("customer_id", customer_id);
    }

    public static Customer delete(int customer_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Customer customerToDelete = getBy("customer_id", customer_id);
        String query = "DELETE FROM customer WHERE customer_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, customer_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return customerToDelete;
    }
}
