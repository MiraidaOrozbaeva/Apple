package org.example.db.db_utils;

import lombok.Getter;
import org.example.utils.file.ConfigurationManager;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;

public class DatabaseConnection {

    @Getter
    private static Connection connection;
    private static Statement statement;

    private DatabaseConnection(){
        // singleton pattern
        // private конструктор — запрещает писать new DB_Connection() снаружи.
        // Все методы static, значит объект вообще не нужен.
        // Это и есть Singleton — класс существует в единственном экземпляре
    }

    private static PGSimpleDataSource getBaseDataSource(String dataBase){
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();  // PGSimpleDataSource — это "описание" как подключиться к БД, сам коннект ещё не открыт
            pgSimpleDataSource.setServerName(ConfigurationManager.getBaseConfig().server()); // {{...}} — Double Brace Initialization, анонимный блок инициализации. Можно написать и обычно:
            pgSimpleDataSource.setPortNumber(ConfigurationManager.getBaseConfig().port()); // можно и так, результат одинаковый: PGSimpleDataSource ds = new PGSimpleDataSource(); ds.setServerName(...); ds.setPortNumber(...);
            pgSimpleDataSource.setUser(ConfigurationManager.getBaseConfig().user());
            pgSimpleDataSource.setPassword(ConfigurationManager.getBaseConfig().dbPassword());
            pgSimpleDataSource.setDatabaseName(dataBase);
        return pgSimpleDataSource;
    }

    public static void openConnection(String database) throws SQLException { // подключение может упасть (нет сети, неверный пароль), поэтому обязаны пробросить исключение наверх
        if (connection == null){ // защита от двойного открытия. Без этой проверки каждый вызов открывал бы новое соединение, старое терялось бы и засоряло БД
            connection = getBaseDataSource(database).getConnection(); // вот здесь реально происходит физическое подключение к БД
            statement = connection.createStatement(); // создаём Statement привязанный к этому соединению. Он нужен для простых запросов без ? параметров
        }
    }

    public static void closeConnection(){
        try {
            if (statement != null){
                statement.close(); // Закрываем сначала Statement, потом Connection — всегда в обратном порядке открытия. Если закрыть Connection первым — Statement зависнет
                statement = null; // = null после закрытия — сбрасываем ссылки чтобы if (connection == null) в openConnection снова сработал
            }
            if (connection != null){
                connection.close();
                connection = null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
//    select * from users;
//    select * from users where user.name = ? and id = ? --> String query
//    'john', 5 --> Object params ('john' -> begin from index 1, 5 -> index 2)
//                                                 Object... params — varargs, можно передать любое количество параметров любого типа
    public static ResultSet makeQuery(String query, Object... params) throws SQLException {
        if (params.length == 0){
            return statement.executeQuery(query);
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement(query); // Почему PreparedStatement а не просто строку склеить? Защита от SQL-инъекций. Если написать "WHERE name = '" + name + "'" и пользователь введёт '; DROP TABLE actor; -- — БД это выполнит. PreparedStatement экранирует всё автоматически
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i+1, params[i]); // select * from users where id = ? and name = '?', '5', 'alex'
            }
            return preparedStatement.executeQuery(); // executeQuery — для SELECT, всегда возвращает ResultSet
        }
    }

    public static int makeUpdate(String query, Object... params) throws SQLException {
        if (params.length == 0){
            return statement.executeUpdate(query);
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i+1, params[i]); // select * from users where id = ? and name = '?', '5', 'alex'
            }
            return preparedStatement.executeUpdate();
        }
    }

    public static ResultSet makeInsert(String query, Object... params) throws SQLException {
//        Нет проверки if (params.length == 0) — INSERT без значений не имеет смысла
        PreparedStatement preparedStatement = connection.prepareStatement(
                query, Statement.RETURN_GENERATED_KEYS // Почему отдельный метод, а не как в makeUpdate? Потому что нужен специальный флаг RETURN_GENERATED_KEYS при создании PreparedStatement — без него БД не вернёт id
        );
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.executeUpdate();
        return preparedStatement.getGeneratedKeys(); // getGeneratedKeys() возвращает ResultSet с одной строкой и одной колонкой — новым actor_id
    }
}
