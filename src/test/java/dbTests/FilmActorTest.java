package dbTests;

import org.example.db.beans.Film_actor;
import org.example.db.db_utils.DB_Connection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("Film_actor DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmActorTest {

    private static Film_actor createdFilmActor;

    @BeforeAll
    static void setUp() throws SQLException {
        DB_Connection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of film_actor")
    void shouldReturnAllFilmActors() throws SQLException {

        List<Film_actor> list = Film_actor.getAllFromFilmActor();

        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();

        Film_actor first = list.get(0);

        assertThat(first.getActor_id()).isNotNull();
        assertThat(first.getFilm_id()).isNotNull();
        assertThat(first.getLast_update()).isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new film_actor relation")
    void shouldInsertFilmActor() throws SQLException {

        Film_actor filmActor = Film_actor.builder()
                .film_id(1)
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        createdFilmActor = Film_actor.insert(filmActor);

        // ⚠️ может быть null из-за логики getBy(actor_id)
        assertThat(createdFilmActor).isNotNull();
        assertThat(createdFilmActor.getFilm_id()).isEqualTo(1);
    }

    // ========================= GET BY =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY: should return film_actor by actor_id")
    void shouldGetFilmActorByActorId() throws SQLException {

        Film_actor found = Film_actor.getBy("actor_id", createdFilmActor.getActor_id());

        assertThat(found).isNotNull();
        assertThat(found.getActor_id()).isEqualTo(createdFilmActor.getActor_id());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should return null for non-existing actor_id")
    void shouldReturnNullForNonExisting() throws SQLException {

        Film_actor notFound = Film_actor.getBy("actor_id", 999999);

        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should throw exception for invalid column")
    void shouldThrowExceptionForInvalidColumn() {

        assertThatThrownBy(() -> Film_actor.getBy("invalid", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= UPDATE =========================

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update film_id")
    void shouldUpdateFilmId() throws SQLException {

        Integer newFilmId = 2;

        Film_actor updated = Film_actor.update(createdFilmActor.getActor_id(), "film_id", newFilmId);

        assertThat(updated).isNotNull();
        assertThat(updated.getFilm_id()).isEqualTo(newFilmId);
    }

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update last_update")
    void shouldUpdateLastUpdate() throws SQLException {

        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());

        Film_actor updated = Film_actor.update(createdFilmActor.getActor_id(), "last_update", newTimestamp);

        assertThat(updated.getLast_update()).isNotNull();
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {

        assertThatThrownBy(() ->
                Film_actor.update(createdFilmActor.getActor_id(), "invalid", "value")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(9)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete film_actor relation")
    void shouldDeleteFilmActor() throws SQLException {

        Film_actor deleted = Film_actor.delete(createdFilmActor.getActor_id());

        assertThat(deleted).isNotNull();
        assertThat(deleted.getActor_id()).isEqualTo(createdFilmActor.getActor_id());
    }

    @Test
    @Order(10)
    @Tag("REGRESSION")
    @DisplayName("DELETE: relation should not exist after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {

        Film_actor afterDelete = Film_actor.getBy("actor_id", createdFilmActor.getActor_id());

        assertThat(afterDelete).isNull();
    }

    @Test
    @Order(11)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should return null for non-existing relation")
    void shouldReturnNullWhenDeletingNonExisting() throws SQLException {

        Film_actor deleted = Film_actor.delete(999999);

        assertThat(deleted).isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(12)
    @Tag("E2E")
    @DisplayName("E2E: full film_actor lifecycle")
    void shouldPerformFullFilmActorLifecycle() throws SQLException {

        // INSERT
        Film_actor filmActor = Film_actor.builder()
                .film_id(1)
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        Film_actor inserted = Film_actor.insert(filmActor);

        assertThat(inserted).isNotNull();

        int actorId = inserted.getActor_id();

        // GET
        Film_actor fetched = Film_actor.getBy("actor_id", actorId);

        assertThat(fetched).isNotNull();

        // UPDATE
        Film_actor updated = Film_actor.update(actorId, "film_id", 3);

        assertThat(updated.getFilm_id()).isEqualTo(3);

        // DELETE
        Film_actor deleted = Film_actor.delete(actorId);

        assertThat(deleted).isNotNull();

        // VERIFY
        Film_actor afterDelete = Film_actor.getBy("actor_id", actorId);

        assertThat(afterDelete).isNull();
    }
}