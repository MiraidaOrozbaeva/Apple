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

public class Staff {

    Integer staff_id;
    String first_name;
    String last_name;
    Integer address_id;
    String email;
    Integer store_id;
    Boolean active;
    String username;
    String password;
    Timestamp last_update;
//    picture

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("staff_id", "first_name", "last_name", "address_id", "email", "store_id", 
                "active", "username", "password", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Staff> getAllFromStaff() throws SQLException {
        String query = "SELECT * FROM staff;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Staff.class);
        }
    }

    public static Staff getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM staff WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Staff.class);
        }
    }

    public static Staff insert(Staff staff) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO staff (first_name, last_name, address_id, email, store_id, active, " +
                "username, password, last_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                staff.getFirst_name(),
                staff.getLast_name(),
                staff.getAddress_id(),
                staff.getEmail(),
                staff.getStore_id(),
                staff.getActive(),
                staff.getUsername(),
                staff.getPassword(),
                staff.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("staff_id", newId);
            }
            return null;
        }
    }

    public static Staff update(int staff_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE staff SET " + column + " = ? WHERE staff_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, staff_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("staff_id", staff_id);
    }

    public static Staff delete(int staff_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Staff staffToDelete = getBy("staff_id", staff_id);
        String query = "DELETE FROM staff WHERE staff_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, staff_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return staffToDelete;
    }
}
