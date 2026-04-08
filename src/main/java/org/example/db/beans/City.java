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

public class City {

    Integer city_id;
    String city;
    Integer country_id;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("city_id", "city", "country_id", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<City> getAllFromCity() throws SQLException {
        String query = "SELECT * FROM city;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, City.class);
        }
    }

    public static City getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM city WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, City.class);
        }
    }

    public static City insert(City city1) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO city (city, country_id, last_update) VALUES (?, ?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                city1.getCity(),
                city1.getCountry_id(),
                city1.getLast_update())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("city_id", newId);
            }
            return null;
        }
    }

    public static City update(int city_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE city SET " + column + " = ? WHERE city_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, newValue, city_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("city_id", city_id);
    }

    public static City delete(int city_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        City cityToDelete = getBy("city_id", city_id);
        String query = "DELETE FROM city WHERE city_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, city_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return cityToDelete;
    }
}
