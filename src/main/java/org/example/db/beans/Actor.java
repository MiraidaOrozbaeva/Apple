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

public class Actor {

    Integer actor_id;
    String first_name;
    String last_name;
    Timestamp last_update;

    // вспомогательный метод валидации — приватный, только внутри класса
    private static void validateColumn(String column) {
        List<String> allowedColumns = List.of("actor_id", "first_name", "last_name", "last_update");
        if (!allowedColumns.contains(column)) {
            throw new IllegalArgumentException("Недопустимая колонка: " + column);
        }
    }
    private static void printQueryResult(String operation, long duration) {
        long secs = duration / 1000;
        long msecs = duration % 1000;
        System.out.println(operation + "  Query returned successfully in " + secs + " secs " + msecs + " msec.");
    }

    public static List<Actor> getAllFromActor() throws SQLException {
        String query = "SELECT * FROM actor;";
        try(ResultSet resultSet = DB_Connection.makeQuery(query)){
            return new BeanProcessor().toBeanList(resultSet, Actor.class);
        }
    }

    public static Actor getBy(String column, int value) throws SQLException {
        validateColumn(column);
        String query = "SELECT * FROM actor WHERE " + column + " = ?; ";
        ResultSet resultSet = DB_Connection.makeQuery(query, value);
        if (!resultSet.next()){
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, Actor.class);
        }
    }

    public static Actor insert(Actor actor) throws SQLException {
        long startTime = System.currentTimeMillis(); // запоминаем время начала

        String query = "INSERT INTO actor (first_name, last_name, last_update) VALUES (?, ?, ?)";
        try (ResultSet generatedKeys = DB_Connection.makeInsert(query,
                actor.getFirst_name(),
                actor.getLast_name(),
                actor.getLast_update())) {

            long endTime = System.currentTimeMillis(); // время конца
            long duration = endTime - startTime;       // сколько прошло миллисекунд

            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                printQueryResult("INSERT 0 1", duration); // ← просто передаём готовое duration
                return getBy("actor_id", newId); // возвращаем полный объект из БД
            }
            return null;
        }
    }

    public static Actor update(int actorId, String column, Object newValue) throws SQLException {
        long startTime = System.currentTimeMillis();

        validateColumn(column);
        String query = "UPDATE actor SET " + column + " = ? WHERE actor_id = ?";
        // в update (возвращает int — количество затронутых строк):
        int affectedRows = DB_Connection.makeUpdate(query, newValue, actorId);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("UPDATE " + affectedRows, duration);

        return getBy("actor_id", actorId);
    }

    public static Actor delete(int actorId) throws SQLException {
        long startTime = System.currentTimeMillis();

        Actor actorToDelete = getBy("actor_id", actorId); // сохраняем до удаления
        String query = "DELETE FROM actor WHERE actor_id = ?";
        int affectedRows = DB_Connection.makeUpdate(query, actorId); // ← сохраняем результат

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        printQueryResult("DELETE " + affectedRows, duration); // ← реальное количество

        return actorToDelete; // возвращаем кто был удалён
    }
}
