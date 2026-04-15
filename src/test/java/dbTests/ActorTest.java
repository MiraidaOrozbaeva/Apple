package dbTests;

import org.example.db.beans.Actor;
import org.example.db.db_utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("DB")
@DisplayName("Actor DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ActorTest {
    private static Actor createdActor;

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseConnection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of actors")
    void shouldReturnAllActors() throws SQLException {
        List<Actor> actors = Actor.getAllFromActor();

        assertThat(actors)
                .as("Actors list should not be null")
                .isNotNull();
        assertThat(actors)
                .as("Actors list should not be empty")
                .isNotEmpty();
        assertThat(actors.get(0).getActor_id())
                .as("First actor should have id")
                .isNotNull();
        assertThat(actors.get(0).getFirst_name())
                .as("First actor should have first name")
                .isNotBlank();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new actor and return it with generated id")
    void shouldInsertNewActor() throws SQLException {
        Actor actorToInsert = Actor.builder()
                .first_name("John")
                .last_name("Doe")
                .last_update(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        createdActor = Actor.insert(actorToInsert);

        assertThat(createdActor)
                .as("Created actor should not be null")
                .isNotNull();
        assertThat(createdActor.getActor_id())
                .as("Created actor should have generated id")
                .isNotNull()
                .isPositive();
        assertThat(createdActor.getFirst_name())
                .as("First name mismatch")
                .isEqualTo(actorToInsert.getFirst_name());
        assertThat(createdActor.getLast_name())
                .as("Last name mismatch")
                .isEqualTo(actorToInsert.getLast_name());
    }

    // ========================= GET BY ID =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY ID: should return correct actor by actor_id")
    void shouldGetActorById() throws SQLException {
        int actorId = createdActor.getActor_id();

        Actor foundActor = Actor.getBy("actor_id", actorId);

        assertThat(foundActor)
                .as("Found actor should not be null")
                .isNotNull();
        assertThat(foundActor.getActor_id())
                .as("Actor id mismatch")
                .isEqualTo(actorId);
        assertThat(foundActor.getFirst_name())
                .as("First name mismatch")
                .isEqualTo(createdActor.getFirst_name());
        assertThat(foundActor.getLast_name())
                .as("Last name mismatch")
                .isEqualTo(createdActor.getLast_name());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should return null for non-existing actor")
    void shouldReturnNullForNonExistingActor() throws SQLException {
        Actor notFound = Actor.getBy("actor_id", 999999);

        assertThat(notFound)
                .as("Non-existing actor should return null")
                .isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should throw exception for invalid column name")
    void shouldThrowExceptionForInvalidColumn() {
        assertThatThrownBy(() -> Actor.getBy("invalid_column", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= UPDATE =========================

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update first_name and return updated actor")
    void shouldUpdateActorFirstName() throws SQLException {
        int actorId = createdActor.getActor_id();
        String newFirstName = "UpdatedJohn";

        Actor updatedActor = Actor.update(actorId, "first_name", newFirstName);

        assertThat(updatedActor)
                .as("Updated actor should not be null")
                .isNotNull();
        assertThat(updatedActor.getFirst_name())
                .as("First name should be updated")
                .isEqualTo(newFirstName);
        assertThat(updatedActor.getLast_name())
                .as("Last name should not change")
                .isEqualTo(createdActor.getLast_name());
        assertThat(updatedActor.getActor_id())
                .as("Actor id should not change")
                .isEqualTo(actorId);
    }

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update last_name and return updated actor")
    void shouldUpdateActorLastName() throws SQLException {
        int actorId = createdActor.getActor_id();
        String newLastName = "UpdatedDoe";

        Actor updatedActor = Actor.update(actorId, "last_name", newLastName);

        assertThat(updatedActor.getLast_name())
                .as("Last name should be updated")
                .isEqualTo(newLastName);
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column name")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {
        assertThatThrownBy(() -> Actor.update(createdActor.getActor_id(), "invalid_column", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(9)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete actor and return deleted actor data")
    void shouldDeleteActor() throws SQLException {
        int actorId = createdActor.getActor_id();

        Actor deletedActor = Actor.delete(actorId);

        assertThat(deletedActor)
                .as("Deleted actor should not be null")
                .isNotNull();
        assertThat(deletedActor.getActor_id())
                .as("Deleted actor id mismatch")
                .isEqualTo(actorId);
    }

    @Test
    @Order(10)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should not exist in DB after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {
        int actorId = createdActor.getActor_id();

        Actor actorAfterDelete = Actor.getBy("actor_id", actorId);

        assertThat(actorAfterDelete)
                .as("Actor should not exist after deletion")
                .isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(11)
    @Tag("E2E")
    @DisplayName("E2E: insert → get by id → update → delete actor")
    void shouldPerformFullActorLifecycle() throws SQLException {

        // INSERT
        Actor newActor = Actor.builder()
                .first_name("Jane")
                .last_name("Smith")
                .last_update(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        Actor inserted = Actor.insert(newActor);

        assertThat(inserted).isNotNull();
        assertThat(inserted.getActor_id()).isPositive();
        assertThat(inserted.getFirst_name()).isEqualTo("Jane");

        int id = inserted.getActor_id();

        // GET BY ID
        Actor fetched = Actor.getBy("actor_id", id);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getFirst_name()).isEqualTo("Jane");

        // UPDATE
        Actor updated = Actor.update(id, "first_name", "JaneUpdated");
        assertThat(updated.getFirst_name()).isEqualTo("JaneUpdated");
        assertThat(updated.getLast_name()).isEqualTo("Smith");

        // DELETE
        Actor deleted = Actor.delete(id);
        assertThat(deleted).isNotNull();
        assertThat(deleted.getActor_id()).isEqualTo(id);

        // VERIFY DELETED
        Actor afterDelete = Actor.getBy("actor_id", id);
        assertThat(afterDelete).isNull();
    }
}
