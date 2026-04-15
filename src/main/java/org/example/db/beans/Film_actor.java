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

public class Film_actor {

    Integer actor_id;
    Integer film_id;
    Timestamp last_update;

    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("actor_id", "film_id", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }

    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Film_actor> getAllFromFilmActor() throws SQLException {
        String query = "SELECT * FROM film_actor;";
        try (ResultSet resultSet = DatabaseConnection.makeQuery(query)) {
            return new BeanProcessor().toBeanList(resultSet, Film_actor.class);
        }
    }

    public static Film_actor getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM film_actor WHERE " + column + " = ?; ";
        try (ResultSet rs = DatabaseConnection.makeQuery(query, value)) { // ← try-with-resources
            return rs.next() ? new BeanProcessor().toBean(rs, Film_actor.class) : null;
        }
    }

    public static Film_actor insert(Film_actor filmActor) throws SQLException {
        long startTime = System.currentTimeMillis();

        String query = "INSERT INTO film_actor (actor_id, film_id, last_update) VALUES (?, ?, ?)";
        try (ResultSet generatedKeys = DatabaseConnection.makeInsert(query,
                filmActor.getActor_id(),
                filmActor.getFilm_id(),
                filmActor.getLast_update())) {

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration);
                return getBy("actor_id", newId);
            }
            return null;
        }
    }

    public static Film_actor update(int actor_id, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE film_actor SET " + column + " = ? WHERE actor_id = ?";
        int affectedRows = DatabaseConnection.makeUpdate(query, newValue, actor_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("actor_id", actor_id);
    }

    public static Film_actor delete(int actor_id) throws SQLException {
        long startTime = System.currentTimeMillis();

        Film_actor filmActorToDelete = getBy("actor_id", actor_id);
        String query = "DELETE FROM film_actor WHERE actor_id = ?";
        int affectedRows = DatabaseConnection.makeUpdate(query, actor_id);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration);

        return filmActorToDelete;
    }
}
