package dbTests;

import org.example.db.beans.City;
import org.example.db.db_utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("City DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CityTest {

    private static City createdCity;

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseConnection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of cities")
    void shouldReturnAllCities() throws SQLException {

        List<City> cities = City.getAllFromCity();

        assertThat(cities).isNotNull();
        assertThat(cities).isNotEmpty();

        City first = cities.get(0);

        assertThat(first.getCity_id()).isNotNull().isPositive();
        assertThat(first.getCity()).isNotBlank();
        assertThat(first.getCountry_id()).isNotNull();
        assertThat(first.getLast_update()).isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new city and return it with generated id")
    void shouldInsertNewCity() throws SQLException {

        City cityToInsert = City.builder()
                .city("Test City")
                .country_id(1)
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        createdCity = City.insert(cityToInsert);

        assertThat(createdCity).isNotNull();
        assertThat(createdCity.getCity_id()).isNotNull().isPositive();
        assertThat(createdCity.getCity()).isEqualTo("Test City");
        assertThat(createdCity.getCountry_id()).isEqualTo(1);
        assertThat(createdCity.getLast_update()).isNotNull();
    }

    // ========================= GET BY =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY ID: should return correct city")
    void shouldGetCityById() throws SQLException {

        City found = City.getBy("city_id", createdCity.getCity_id());

        assertThat(found).isNotNull();
        assertThat(found.getCity_id()).isEqualTo(createdCity.getCity_id());
        assertThat(found.getCity()).isEqualTo(createdCity.getCity());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should return null for non-existing city")
    void shouldReturnNullForNonExistingCity() throws SQLException {

        City notFound = City.getBy("city_id", 999999);

        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should throw exception for invalid column")
    void shouldThrowExceptionForInvalidColumn() {

        assertThatThrownBy(() -> City.getBy("invalid_column", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should find city by country_id")
    void shouldGetCityByCountryId() throws SQLException {

        City found = City.getBy("country_id", createdCity.getCountry_id());

        assertThat(found).isNotNull();
        assertThat(found.getCountry_id()).isEqualTo(createdCity.getCountry_id());
    }

    // ========================= UPDATE =========================

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update city name")
    void shouldUpdateCityName() throws SQLException {

        String newName = "Updated City";

        City updated = City.update(createdCity.getCity_id(), "city", newName);

        assertThat(updated).isNotNull();
        assertThat(updated.getCity()).isEqualTo(newName);
        assertThat(updated.getCity_id()).isEqualTo(createdCity.getCity_id());
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update country_id")
    void shouldUpdateCountryId() throws SQLException {

        Integer newCountryId = 2;

        City updated = City.update(createdCity.getCity_id(), "country_id", newCountryId);

        assertThat(updated.getCountry_id()).isEqualTo(newCountryId);
    }

    @Test
    @Order(9)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update last_update field")
    void shouldUpdateLastUpdate() throws SQLException {

        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());

        City updated = City.update(createdCity.getCity_id(), "last_update", newTimestamp);

        assertThat(updated.getLast_update()).isNotNull();
    }

    @Test
    @Order(10)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {

        assertThatThrownBy(() ->
                City.update(createdCity.getCity_id(), "invalid", "value")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(11)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete city and return deleted data")
    void shouldDeleteCity() throws SQLException {

        City deleted = City.delete(createdCity.getCity_id());

        assertThat(deleted).isNotNull();
        assertThat(deleted.getCity_id()).isEqualTo(createdCity.getCity_id());
        assertThat(deleted.getCity()).isEqualTo("Updated City");
    }

    @Test
    @Order(12)
    @Tag("REGRESSION")
    @DisplayName("DELETE: city should not exist after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {

        City afterDelete = City.getBy("city_id", createdCity.getCity_id());

        assertThat(afterDelete).isNull();
    }

    @Test
    @Order(13)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should return null for non-existing city")
    void shouldReturnNullWhenDeletingNonExisting() throws SQLException {

        City deleted = City.delete(999999);

        assertThat(deleted).isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(14)
    @Tag("E2E")
    @DisplayName("E2E: insert → get → update → delete city")
    void shouldPerformFullCityLifecycle() throws SQLException {

        // INSERT
        City city = City.builder()
                .city("E2E City")
                .country_id(1)
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        City inserted = City.insert(city);

        assertThat(inserted).isNotNull();
        assertThat(inserted.getCity_id()).isPositive();

        int id = inserted.getCity_id();

        // GET
        City fetched = City.getBy("city_id", id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getCity()).isEqualTo("E2E City");

        // UPDATE
        City updated = City.update(id, "city", "E2E Updated");

        assertThat(updated.getCity()).isEqualTo("E2E Updated");

        // DELETE
        City deleted = City.delete(id);

        assertThat(deleted).isNotNull();

        // VERIFY
        City afterDelete = City.getBy("city_id", id);

        assertThat(afterDelete).isNull();
    }
}