package dbTests;

import org.example.db.beans.Customer;
import org.example.db.db_utils.DB_Connection;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("Customer DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest {

    private static Customer createdCustomer;

    @BeforeAll
    static void setUp() throws SQLException {
        DB_Connection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of customers")
    void shouldReturnAllCustomers() throws SQLException {

        List<Customer> customers = Customer.getAllFromCustomer();

        assertThat(customers).isNotNull();
        assertThat(customers).isNotEmpty();

        Customer first = customers.get(0);

        assertThat(first.getCustomer_id()).isNotNull().isPositive();
        assertThat(first.getFirst_name()).isNotBlank();
        assertThat(first.getLast_name()).isNotBlank();
        assertThat(first.getStore_id()).isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new customer")
    void shouldInsertNewCustomer() throws SQLException {

        Customer customerToInsert = Customer.builder()
                .store_id(1)
                .first_name("John")
                .last_name("Doe")
                .email("john.doe@test.com")
                .address_id(1)
                .activebool(true)
                .create_date(new Date(System.currentTimeMillis()))
                .last_update(new Timestamp(System.currentTimeMillis()))
                .active(1)
                .build();

        createdCustomer = Customer.insert(customerToInsert);

        assertThat(createdCustomer).isNotNull();
        assertThat(createdCustomer.getCustomer_id()).isNotNull().isPositive();
        assertThat(createdCustomer.getFirst_name()).isEqualTo("John");
        assertThat(createdCustomer.getLast_name()).isEqualTo("Doe");
        assertThat(createdCustomer.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(createdCustomer.getActivebool()).isTrue();
        assertThat(createdCustomer.getActive()).isEqualTo(1);
    }

    // ========================= GET BY =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY ID: should return correct customer")
    void shouldGetCustomerById() throws SQLException {

        Customer found = Customer.getBy("customer_id", createdCustomer.getCustomer_id());

        assertThat(found).isNotNull();
        assertThat(found.getCustomer_id()).isEqualTo(createdCustomer.getCustomer_id());
        assertThat(found.getEmail()).isEqualTo(createdCustomer.getEmail());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should return null for non-existing customer")
    void shouldReturnNullForNonExistingCustomer() throws SQLException {

        Customer notFound = Customer.getBy("customer_id", 999999);

        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY: should throw exception for invalid column")
    void shouldThrowExceptionForInvalidColumn() {

        assertThatThrownBy(() -> Customer.getBy("invalid_column", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= UPDATE =========================

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update email")
    void shouldUpdateEmail() throws SQLException {

        String newEmail = "updated@test.com";

        Customer updated = Customer.update(createdCustomer.getCustomer_id(), "email", newEmail);

        assertThat(updated).isNotNull();
        assertThat(updated.getEmail()).isEqualTo(newEmail);
    }

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update active flag")
    void shouldUpdateActiveFlag() throws SQLException {

        Customer updated = Customer.update(createdCustomer.getCustomer_id(), "active", 0);

        assertThat(updated.getActive()).isEqualTo(0);
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update last_update")
    void shouldUpdateLastUpdate() throws SQLException {

        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());

        Customer updated = Customer.update(createdCustomer.getCustomer_id(), "last_update", newTimestamp);

        assertThat(updated.getLast_update()).isNotNull();
    }

    @Test
    @Order(9)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {

        assertThatThrownBy(() ->
                Customer.update(createdCustomer.getCustomer_id(), "invalid", "value")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(10)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete customer")
    void shouldDeleteCustomer() throws SQLException {

        Customer deleted = Customer.delete(createdCustomer.getCustomer_id());

        assertThat(deleted).isNotNull();
        assertThat(deleted.getCustomer_id()).isEqualTo(createdCustomer.getCustomer_id());
        assertThat(deleted.getEmail()).isEqualTo("updated@test.com");
    }

    @Test
    @Order(11)
    @Tag("REGRESSION")
    @DisplayName("DELETE: customer should not exist after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {

        Customer afterDelete = Customer.getBy("customer_id", createdCustomer.getCustomer_id());

        assertThat(afterDelete).isNull();
    }

    @Test
    @Order(12)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should return null for non-existing customer")
    void shouldReturnNullWhenDeletingNonExisting() throws SQLException {

        Customer deleted = Customer.delete(999999);

        assertThat(deleted).isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(13)
    @Tag("E2E")
    @DisplayName("E2E: full customer lifecycle")
    void shouldPerformFullCustomerLifecycle() throws SQLException {

        // INSERT
        Customer customer = Customer.builder()
                .store_id(1)
                .first_name("E2E")
                .last_name("User")
                .email("e2e@test.com")
                .address_id(1)
                .activebool(true)
                .create_date(new Date(System.currentTimeMillis()))
                .last_update(new Timestamp(System.currentTimeMillis()))
                .active(1)
                .build();

        Customer inserted = Customer.insert(customer);

        assertThat(inserted).isNotNull();

        int id = inserted.getCustomer_id();

        // GET
        Customer fetched = Customer.getBy("customer_id", id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getEmail()).isEqualTo("e2e@test.com");

        // UPDATE
        Customer updated = Customer.update(id, "email", "e2e_updated@test.com");

        assertThat(updated.getEmail()).isEqualTo("e2e_updated@test.com");

        // DELETE
        Customer deleted = Customer.delete(id);

        assertThat(deleted).isNotNull();

        // VERIFY
        Customer afterDelete = Customer.getBy("customer_id", id);

        assertThat(afterDelete).isNull();
    }
}