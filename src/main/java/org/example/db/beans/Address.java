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

public class Address {
    Integer address_id;
    String address;
    String address2;
    String district;
    Integer city_id;
    String postal_code;
    String phone;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("address_id", "address", "address2", "district", "city_id",
                "postal_code", "phone", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Address> getAllFromAddress() throws SQLException {
        String query = "SELECT * FROM address;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Address.class);
        }
    }

    public static Address getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM address WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Address.class);
        }
    }

    public static Address insert(Address address1) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO address (address, address2, district, city_id, postal_code, phone, last_update) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                address1.getAddress(),
                address1.getAddress2(),
                address1.getDistrict(),
                address1.getCity_id(),
                address1.getPostal_code(),
                address1.getPhone())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("address_id", newId);
            }
            return null;
        }
    }

    public static Address update(int address_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE address SET " + column + " = ? WHERE address_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, address_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("address_id", address_id);
    }

    public static Address delete(int address_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Address addressToDelete = getBy("address_id", address_id);
        String query = "DELETE FROM address WHERE address_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, address_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return addressToDelete;
    }
}
