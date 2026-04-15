package dbTests;

import org.example.db.beans.Film_category;
import org.example.db.db_utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("Film_category DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmCategoryTest {

    private static Film_category createdFilmCategory;

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseConnection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of film_category")
    void shouldReturnAllFilmCategories() throws SQLException {

        List<Film_category> list = Film_category.getAllFromFilmCategory();

        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();

        Film_category first = list.get(0);

        assertThat(first.getFilm_id()).isNotNull();
        assertThat(first.getCategory_id()).isNotNull();
        assertThat(first.getLast_update()).isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new film_category relation")
    void shouldInsertFilmCategory() throws SQLException {

        Film_category filmCategory = Film_category.builder()
                .category_id(1)
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        createdFilmCategory = Film_category.insert(filmCategory);

        // ⚠️ может быть null из-за логики getBy(film_id)
        assertThat(createdFilmCategory).isNotNull();
        assertThat(createdFilmCategory.getCategory_id()).isEqualTo(1);
    }

    // ========================= GET BY =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY: should return relation by film_id")
    void shouldGetByFilmId() throws SQLException {

        Film_category found = Film_category.getBy("film_id", createdFilmCategory.getFilm_id());

        assertThat(found).isNotNull();
        assertThat(found.getFilm_id()).isEqualTo(createdFilmCategory.getFilm_id());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should return null for non-existing film_id")
    void shouldReturnNullForNonExisting() throws SQLException {

        Film_category notFound = Film_category.getBy("film_id", 999999);

        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should throw exception for invalid column")
    void shouldThrowExceptionForInvalidColumn() {

        assertThatThrownBy(() -> Film_category.getBy("invalid", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= UPDATE =========================

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update category_id")
    void shouldUpdateCategoryId() throws SQLException {

        Integer newCategoryId = 2;

        Film_category updated = Film_category.update(createdFilmCategory.getFilm_id(), "category_id", newCategoryId);

        assertThat(updated).isNotNull();
        assertThat(updated.getCategory_id()).isEqualTo(newCategoryId);
    }

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update last_update")
    void shouldUpdateLastUpdate() throws SQLException {

        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());

        Film_category updated = Film_category.update(createdFilmCategory.getFilm_id(), "last_update", newTimestamp);

        assertThat(updated.getLast_update()).isNotNull();
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {

        assertThatThrownBy(() ->
                Film_category.update(createdFilmCategory.getFilm_id(), "invalid", "value")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(9)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete relation")
    void shouldDeleteFilmCategory() throws SQLException {

        Film_category deleted = Film_category.delete(createdFilmCategory.getFilm_id());

        assertThat(deleted).isNotNull();
        assertThat(deleted.getFilm_id()).isEqualTo(createdFilmCategory.getFilm_id());
    }

    @Test
    @Order(10)
    @Tag("REGRESSION")
    @DisplayName("DELETE: relation should not exist after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {

        Film_category afterDelete = Film_category.getBy("film_id", createdFilmCategory.getFilm_id());

        assertThat(afterDelete).isNull();
    }

    @Test
    @Order(11)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should return null for non-existing relation")
    void shouldReturnNullWhenDeletingNonExisting() throws SQLException {

        Film_category deleted = Film_category.delete(999999);

        assertThat(deleted).isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(12)
    @Tag("E2E")
    @DisplayName("E2E: full film_category lifecycle")
    void shouldPerformFullFilmCategoryLifecycle() throws SQLException {

        // INSERT
        Film_category filmCategory = Film_category.builder()
                .category_id(1)
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        Film_category inserted = Film_category.insert(filmCategory);

        assertThat(inserted).isNotNull();

        int filmId = inserted.getFilm_id();

        // GET
        Film_category fetched = Film_category.getBy("film_id", filmId);

        assertThat(fetched).isNotNull();

        // UPDATE
        Film_category updated = Film_category.update(filmId, "category_id", 3);

        assertThat(updated.getCategory_id()).isEqualTo(3);

        // DELETE
        Film_category deleted = Film_category.delete(filmId);

        assertThat(deleted).isNotNull();

        // VERIFY
        Film_category afterDelete = Film_category.getBy("film_id", filmId);

        assertThat(afterDelete).isNull();
    }
}