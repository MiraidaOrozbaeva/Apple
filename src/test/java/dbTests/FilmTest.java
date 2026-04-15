package dbTests;

import org.example.db.beans.Film;
import org.example.db.db_utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("Film DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmTest {

    private static Film createdFilm;

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseConnection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of films")
    void shouldReturnAllFilms() throws SQLException {

        List<Film> films = Film.getAllFromFilm();

        assertThat(films).isNotNull();
        assertThat(films).isNotEmpty();

        Film first = films.get(0);

        assertThat(first.getFilm_id()).isNotNull().isPositive();
        assertThat(first.getTitle()).isNotBlank();
        assertThat(first.getLanguage_id()).isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new film")
    void shouldInsertNewFilm() throws SQLException {

        Film filmToInsert = Film.builder()
                .title("Test Film")
                .description("Test Description")
                .release_year(2024)
                .language_id(1)
                .rental_duration(5)
                .rental_rate(2.99)
                .length(120)
                .replacement_cost(19.99)
                .rating("PG")
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        createdFilm = Film.insert(filmToInsert);

        assertThat(createdFilm).isNotNull();
        assertThat(createdFilm.getFilm_id()).isNotNull().isPositive();
        assertThat(createdFilm.getTitle()).isEqualTo("Test Film");
        assertThat(createdFilm.getDescription()).isEqualTo("Test Description");
        assertThat(createdFilm.getRelease_year()).isEqualTo(2024);
        assertThat(createdFilm.getRating()).isEqualTo("PG");
    }

    // ========================= GET BY =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY ID: should return correct film")
    void shouldGetFilmById() throws SQLException {

        Film found = Film.getBy("film_id", createdFilm.getFilm_id());

        assertThat(found).isNotNull();
        assertThat(found.getFilm_id()).isEqualTo(createdFilm.getFilm_id());
        assertThat(found.getTitle()).isEqualTo(createdFilm.getTitle());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should return null for non-existing film")
    void shouldReturnNullForNonExistingFilm() throws SQLException {

        Film notFound = Film.getBy("film_id", 999999);

        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should throw exception for invalid column")
    void shouldThrowExceptionForInvalidColumn() {

        assertThatThrownBy(() -> Film.getBy("invalid_column", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= UPDATE =========================

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update title")
    void shouldUpdateTitle() throws SQLException {

        String newTitle = "Updated Film";

        Film updated = Film.update(createdFilm.getFilm_id(), "title", newTitle);

        assertThat(updated).isNotNull();
        assertThat(updated.getTitle()).isEqualTo(newTitle);
    }

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update rental_rate")
    void shouldUpdateRentalRate() throws SQLException {

        Double newRate = 4.99;

        Film updated = Film.update(createdFilm.getFilm_id(), "rental_rate", newRate);

        assertThat(updated.getRental_rate()).isEqualTo(newRate);
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update length")
    void shouldUpdateLength() throws SQLException {

        Integer newLength = 150;

        Film updated = Film.update(createdFilm.getFilm_id(), "length", newLength);

        assertThat(updated.getLength()).isEqualTo(newLength);
    }

    @Test
    @Order(9)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {

        assertThatThrownBy(() ->
                Film.update(createdFilm.getFilm_id(), "invalid", "value")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(10)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete film")
    void shouldDeleteFilm() throws SQLException {

        Film deleted = Film.delete(createdFilm.getFilm_id());

        assertThat(deleted).isNotNull();
        assertThat(deleted.getFilm_id()).isEqualTo(createdFilm.getFilm_id());
        assertThat(deleted.getTitle()).isEqualTo("Updated Film");
    }

    @Test
    @Order(11)
    @Tag("REGRESSION")
    @DisplayName("DELETE: film should not exist after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {

        Film afterDelete = Film.getBy("film_id", createdFilm.getFilm_id());

        assertThat(afterDelete).isNull();
    }

    @Test
    @Order(12)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should return null for non-existing film")
    void shouldReturnNullWhenDeletingNonExisting() throws SQLException {

        Film deleted = Film.delete(999999);

        assertThat(deleted).isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(13)
    @Tag("E2E")
    @DisplayName("E2E: full film lifecycle")
    void shouldPerformFullFilmLifecycle() throws SQLException {

        // INSERT
        Film film = Film.builder()
                .title("E2E Film")
                .description("E2E Description")
                .release_year(2023)
                .language_id(1)
                .rental_duration(3)
                .rental_rate(1.99)
                .length(90)
                .replacement_cost(15.99)
                .rating("G")
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        Film inserted = Film.insert(film);

        assertThat(inserted).isNotNull();

        int id = inserted.getFilm_id();

        // GET
        Film fetched = Film.getBy("film_id", id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getTitle()).isEqualTo("E2E Film");

        // UPDATE
        Film updated = Film.update(id, "title", "E2E Updated");

        assertThat(updated.getTitle()).isEqualTo("E2E Updated");

        // DELETE
        Film deleted = Film.delete(id);

        assertThat(deleted).isNotNull();

        // VERIFY
        Film afterDelete = Film.getBy("film_id", id);

        assertThat(afterDelete).isNull();
    }
}