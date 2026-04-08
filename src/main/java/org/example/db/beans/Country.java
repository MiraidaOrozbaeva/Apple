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

public class Country {

    Integer country_id;
    String country;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("country_id", "country", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Country> getAllFromCountry() throws SQLException {
        String query = "SELECT * FROM country;";
        try (ResultSet resultSet = DB_Connection.makeQuery(query)) {
            return new BeanProcessor().toBeanList(resultSet, Country.class);
        }
    }

    public static Country getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM country WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()) {
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Country.class);
        }
    }

    public static Country insert(Country country1) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO country (country, last_update) VALUES (?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                country1.getCountry(),
                country1.getLast_update())) {

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("country_id", newId);
            }
            return null;
        }
    }

    public static Country update(int country_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE country SET " + column + " = ? WHERE country_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, country_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("country_id", country_id);
    }

    public static Country delete(int country_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Country countryToDelete = getBy("country_id", country_id);
        String query = "DELETE FROM country WHERE country_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, country_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return countryToDelete;
    }
}
