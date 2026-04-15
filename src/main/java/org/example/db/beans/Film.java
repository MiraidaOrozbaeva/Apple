package org.example.db.beans;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbutils.BeanProcessor;
import org.example.db.db_utils.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Setter
@Getter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Film {

    Integer film_id;
    String title;
    String description;
    Integer release_year;
    Integer language_id;
    Integer rental_duration;
    Double rental_rate;
    Integer length;
    Double replacement_cost;
    String rating; // mpaa_rating ??
    Timestamp last_update; // without time zone ??
    String[] special_features;
    Map<String, Integer> fulltext; // tsvector ??

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("film_id", "title", "description", "release_year", "language_id",
                "rental_duration", "rental_rate", "length", "replacement_cost", "rating", "last_update",
                "special_features", "fulltext");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Film> getAllFromFilm() throws SQLException {
        String query = "SELECT * FROM film;";
        try(ResultSet resultSet = DatabaseConnection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Film.class);
        }
    }

    public static Film getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM film WHERE " + column + " = ?; ";
        ResultSet resultSet = DatabaseConnection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Film.class);
        }
    }

    public static Film insert(Film film) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO film (title, description, release_year, language_id, rental_duration," +
                "rental_rate, length, replacement_cost, rating, last_update, special_features, fulltext) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (ResultSet generatedKeys = DatabaseConnection.makeInsert(query,
                film.getTitle(),
                film.getDescription(),
                film.getRelease_year(),
                film.getLanguage_id(),
                film.getRental_duration(),
                film.getRental_rate(),
                film.getLength(),
                film.getReplacement_cost(),
                film.getRating(),
                film.getLast_update(),
                film.getSpecial_features(),
                film.getFulltext())){

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("film_id", newId);
            }
            return null;
        }
    }

    public static Film update(int film_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE film SET " + column + " = ? WHERE film_id = ?";
        int affectedRows = DatabaseConnection.makeUpdate(query, newValue, film_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("film_id", film_id);
    }

    public static Film delete(int film_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Film filmToDelete = getBy("film_id", film_id);
        String query = "DELETE FROM film WHERE film_id = ?";
        int affectedRows = DatabaseConnection.makeUpdate(query, film_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return filmToDelete;
    }
}
