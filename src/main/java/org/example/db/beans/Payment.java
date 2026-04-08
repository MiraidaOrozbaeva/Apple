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

public class Payment {

    Integer payment_id;
    Integer customer_id;
    Integer staff_id;
    Integer rental_id;
    Double amount;
    Timestamp payment_date;


    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("payment_id", "customer_id", "staff_id", "rental_id", "amount",
                "payment_date");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Payment> getAllCFromPayment() throws SQLException {
        String query = "SELECT * FROM payment;";
        try (ResultSet resultSet = DB_Connection.makeQuery(query)) {
            return new BeanProcessor().toBeanList(resultSet, Payment.class);
        }
    }

    public static Payment getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM payment WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()) {
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Payment.class);
        }
    }

    public static Payment insert(Payment payment) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO payment (customer_id, staff_id, rental_id, amount, payment_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                payment.getCustomer_id(),
                payment.getStaff_id(),
                payment.getRental_id(),
                payment.getAmount(),
                payment.getPayment_date())) {

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("payment_id", newId);
            }
            return null;
        }
    }

    public static Payment update(int payment_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE payment SET " + column + " = ? WHERE payment_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, payment_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("payment_id", payment_id);
    }

    public static Payment delete(int payment_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Payment paymentToDelete = getBy("payment_id", payment_id);
        String query = "DELETE FROM payment WHERE payment_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, payment_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return paymentToDelete;
    }
}
