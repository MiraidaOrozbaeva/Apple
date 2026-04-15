package dbTests;

import org.example.db.beans.Country;
import org.example.db.db_utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("Country DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryTest {

    private static Country createdCountry;

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseConnection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of countries")
    void shouldReturnAllCountries() throws SQLException {

        List<Country> countries = Country.getAllFromCountry();

        assertThat(countries).isNotNull();
        assertThat(countries).isNotEmpty();

        Country first = countries.get(0);

        assertThat(first.getCountry_id()).isNotNull().isPositive();
        assertThat(first.getCountry()).isNotBlank();
        assertThat(first.getLast_update()).isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new country and return it with generated id")
    void shouldInsertNewCountry() throws SQLException {

        Country countryToInsert = Country.builder()
                .country("Test Country")
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        createdCountry = Country.insert(countryToInsert);

        assertThat(createdCountry).isNotNull();
        assertThat(createdCountry.getCountry_id()).isNotNull().isPositive();
        assertThat(createdCountry.getCountry()).isEqualTo("Test Country");
        assertThat(createdCountry.getLast_update()).isNotNull();
    }

    // ========================= GET BY =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY ID: should return correct country")
    void shouldGetCountryById() throws SQLException {

        Country found = Country.getBy("country_id", createdCountry.getCountry_id());

        assertThat(found).isNotNull();
        assertThat(found.getCountry_id()).isEqualTo(createdCountry.getCountry_id());
        assertThat(found.getCountry()).isEqualTo(createdCountry.getCountry());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should return null for non-existing country")
    void shouldReturnNullForNonExistingCountry() throws SQLException {

        Country notFound = Country.getBy("country_id", 999999);

        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should throw exception for invalid column")
    void shouldThrowExceptionForInvalidColumn() {

        assertThatThrownBy(() -> Country.getBy("invalid_column", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= UPDATE =========================

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update country name")
    void shouldUpdateCountryName() throws SQLException {

        String newName = "Updated Country";

        Country updated = Country.update(createdCountry.getCountry_id(), "country", newName);

        assertThat(updated).isNotNull();
        assertThat(updated.getCountry()).isEqualTo(newName);
        assertThat(updated.getCountry_id()).isEqualTo(createdCountry.getCountry_id());
    }

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update last_update field")
    void shouldUpdateLastUpdate() throws SQLException {

        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());

        Country updated = Country.update(createdCountry.getCountry_id(), "last_update", newTimestamp);

        assertThat(updated.getLast_update()).isNotNull();
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {

        assertThatThrownBy(() ->
                Country.update(createdCountry.getCountry_id(), "invalid", "value")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(9)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete country and return deleted data")
    void shouldDeleteCountry() throws SQLException {

        Country deleted = Country.delete(createdCountry.getCountry_id());

        assertThat(deleted).isNotNull();
        assertThat(deleted.getCountry_id()).isEqualTo(createdCountry.getCountry_id());
        assertThat(deleted.getCountry()).isEqualTo("Updated Country");
    }

    @Test
    @Order(10)
    @Tag("REGRESSION")
    @DisplayName("DELETE: country should not exist after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {

        Country afterDelete = Country.getBy("country_id", createdCountry.getCountry_id());

        assertThat(afterDelete).isNull();
    }

    @Test
    @Order(11)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should return null for non-existing country")
    void shouldReturnNullWhenDeletingNonExisting() throws SQLException {

        Country deleted = Country.delete(999999);

        assertThat(deleted).isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(12)
    @Tag("E2E")
    @DisplayName("E2E: insert → get → update → delete country")
    void shouldPerformFullCountryLifecycle() throws SQLException {

        // INSERT
        Country country = Country.builder()
                .country("E2E Country")
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        Country inserted = Country.insert(country);

        assertThat(inserted).isNotNull();
        assertThat(inserted.getCountry_id()).isPositive();

        int id = inserted.getCountry_id();

        // GET
        Country fetched = Country.getBy("country_id", id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getCountry()).isEqualTo("E2E Country");

        // UPDATE
        Country updated = Country.update(id, "country", "E2E Updated");

        assertThat(updated.getCountry()).isEqualTo("E2E Updated");

        // DELETE
        Country deleted = Country.delete(id);

        assertThat(deleted).isNotNull();

        // VERIFY
        Country afterDelete = Country.getBy("country_id", id);

        assertThat(afterDelete).isNull();
    }
}