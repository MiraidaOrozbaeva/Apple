package dbTests;

import org.example.db.beans.Address;
import org.example.db.db_utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("Address DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressTest {
    private static Address createdAddress;

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseConnection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of addresses")
    void shouldReturnAllAddresses() throws SQLException {
        List<Address> addresses = Address.getAllFromAddress();

        assertThat(addresses)
                .as("Addresses list should not be null")
                .isNotNull();
        assertThat(addresses)
                .as("Addresses list should not be empty")
                .isNotEmpty();
        assertThat(addresses.get(0).getAddress_id())
                .as("First address should have id")
                .isNotNull()
                .isPositive();
        assertThat(addresses.get(0).getAddress())
                .as("First address should have address field")
                .isNotBlank();
        assertThat(addresses.get(0).getCity_id())
                .as("First address should have city_id")
                .isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new address and return it with generated id")
    void shouldInsertNewAddress() throws SQLException {
        Address addressToInsert = Address.builder()
                .address("123 Test Street")
                .address2("Apt 4B")
                .district("Test District")
                .city_id(1)
                .postal_code("12345")
                .phone("1234567890")
                .build();

        createdAddress = Address.insert(addressToInsert);

        assertThat(createdAddress)
                .as("Created address should not be null")
                .isNotNull();
        assertThat(createdAddress.getAddress_id())
                .as("Created address should have generated id")
                .isNotNull()
                .isPositive();
        assertThat(createdAddress.getAddress())
                .as("Address field mismatch")
                .isEqualTo(addressToInsert.getAddress());
        assertThat(createdAddress.getAddress2())
                .as("Address2 field mismatch")
                .isEqualTo(addressToInsert.getAddress2());
        assertThat(createdAddress.getDistrict())
                .as("District field mismatch")
                .isEqualTo(addressToInsert.getDistrict());
        assertThat(createdAddress.getCity_id())
                .as("City id mismatch")
                .isEqualTo(addressToInsert.getCity_id());
        assertThat(createdAddress.getPostal_code())
                .as("Postal code mismatch")
                .isEqualTo(addressToInsert.getPostal_code());
        assertThat(createdAddress.getPhone())
                .as("Phone mismatch")
                .isEqualTo(addressToInsert.getPhone());
        assertThat(createdAddress.getLast_update())
                .as("Last update should be set automatically")
                .isNotNull();
    }

    // ========================= GET BY ID =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY ID: should return correct address by address_id")
    void shouldGetAddressById() throws SQLException {
        int addressId = createdAddress.getAddress_id();

        Address foundAddress = Address.getBy("address_id", addressId);

        assertThat(foundAddress)
                .as("Found address should not be null")
                .isNotNull();
        assertThat(foundAddress.getAddress_id())
                .as("Address id mismatch")
                .isEqualTo(addressId);
        assertThat(foundAddress.getAddress())
                .as("Address field mismatch")
                .isEqualTo(createdAddress.getAddress());
        assertThat(foundAddress.getDistrict())
                .as("District mismatch")
                .isEqualTo(createdAddress.getDistrict());
        assertThat(foundAddress.getCity_id())
                .as("City id mismatch")
                .isEqualTo(createdAddress.getCity_id());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should return null for non-existing address")
    void shouldReturnNullForNonExistingAddress() throws SQLException {
        Address notFound = Address.getBy("address_id", 999999);

        assertThat(notFound)
                .as("Non-existing address should return null")
                .isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should throw exception for invalid column name")
    void shouldThrowExceptionForInvalidColumn() {
        assertThatThrownBy(() -> Address.getBy("invalid_column", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should find address by city_id")
    void shouldGetAddressByCityId() throws SQLException {
        Address foundAddress = Address.getBy("city_id", createdAddress.getCity_id());

        assertThat(foundAddress)
                .as("Address found by city_id should not be null")
                .isNotNull();
        assertThat(foundAddress.getCity_id())
                .as("City id mismatch")
                .isEqualTo(createdAddress.getCity_id());
    }

    // ========================= UPDATE =========================

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update address field and return updated address")
    void shouldUpdateAddressField() throws SQLException {
        int addressId = createdAddress.getAddress_id();
        String newAddress = "456 Updated Street";

        Address updatedAddress = Address.update(addressId, "address", newAddress);

        assertThat(updatedAddress)
                .as("Updated address should not be null")
                .isNotNull();
        assertThat(updatedAddress.getAddress())
                .as("Address field should be updated")
                .isEqualTo(newAddress);
        assertThat(updatedAddress.getAddress_id())
                .as("Address id should not change")
                .isEqualTo(addressId);
        assertThat(updatedAddress.getDistrict())
                .as("District should not change")
                .isEqualTo(createdAddress.getDistrict());
        assertThat(updatedAddress.getCity_id())
                .as("City id should not change")
                .isEqualTo(createdAddress.getCity_id());
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update district field and return updated address")
    void shouldUpdateDistrictField() throws SQLException {
        int addressId = createdAddress.getAddress_id();
        String newDistrict = "Updated District";

        Address updatedAddress = Address.update(addressId, "district", newDistrict);

        assertThat(updatedAddress.getDistrict())
                .as("District should be updated")
                .isEqualTo(newDistrict);
    }

    @Test
    @Order(9)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update postal_code field and return updated address")
    void shouldUpdatePostalCodeField() throws SQLException {
        int addressId = createdAddress.getAddress_id();
        String newPostalCode = "99999";

        Address updatedAddress = Address.update(addressId, "postal_code", newPostalCode);

        assertThat(updatedAddress.getPostal_code())
                .as("Postal code should be updated")
                .isEqualTo(newPostalCode);
    }

    @Test
    @Order(10)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update phone field and return updated address")
    void shouldUpdatePhoneField() throws SQLException {
        int addressId = createdAddress.getAddress_id();
        String newPhone = "9876543210";

        Address updatedAddress = Address.update(addressId, "phone", newPhone);

        assertThat(updatedAddress.getPhone())
                .as("Phone should be updated")
                .isEqualTo(newPhone);
    }

    @Test
    @Order(11)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column name")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {
        assertThatThrownBy(() -> Address.update(createdAddress.getAddress_id(), "invalid_column", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(12)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete address and return deleted address data")
    void shouldDeleteAddress() throws SQLException {
        int addressId = createdAddress.getAddress_id();

        Address deletedAddress = Address.delete(addressId);

        assertThat(deletedAddress)
                .as("Deleted address should not be null")
                .isNotNull();
        assertThat(deletedAddress.getAddress_id())
                .as("Deleted address id mismatch")
                .isEqualTo(addressId);
        assertThat(deletedAddress.getAddress())
                .as("Deleted address field mismatch")
                .isEqualTo("456 Updated Street"); // последнее обновлённое значение
    }

    @Test
    @Order(13)
    @Tag("REGRESSION")
    @DisplayName("DELETE: address should not exist in DB after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {
        int addressId = createdAddress.getAddress_id();

        Address addressAfterDelete = Address.getBy("address_id", addressId);

        assertThat(addressAfterDelete)
                .as("Address should not exist after deletion")
                .isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(14)
    @Tag("E2E")
    @DisplayName("E2E: insert → get by id → update → delete address")
    void shouldPerformFullAddressLifecycle() throws SQLException {

        // INSERT
        Address newAddress = Address.builder()
                .address("789 E2E Street")
                .address2("Suite 1")
                .district("E2E District")
                .city_id(1)
                .postal_code("54321")
                .phone("5555555555")
                .build();

        Address inserted = Address.insert(newAddress);

        assertThat(inserted).isNotNull();
        assertThat(inserted.getAddress_id()).isPositive();
        assertThat(inserted.getAddress()).isEqualTo("789 E2E Street");
        assertThat(inserted.getDistrict()).isEqualTo("E2E District");
        assertThat(inserted.getLast_update()).isNotNull();

        int id = inserted.getAddress_id();

        // GET BY ID
        Address fetched = Address.getBy("address_id", id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getAddress()).isEqualTo("789 E2E Street");
        assertThat(fetched.getPostal_code()).isEqualTo("54321");

        // UPDATE
        Address updated = Address.update(id, "address", "999 Updated E2E Street");

        assertThat(updated.getAddress()).isEqualTo("999 Updated E2E Street");
        assertThat(updated.getDistrict()).isEqualTo("E2E District"); // не изменился
        assertThat(updated.getCity_id()).isEqualTo(1);               // не изменился

        // DELETE
        Address deleted = Address.delete(id);

        assertThat(deleted).isNotNull();
        assertThat(deleted.getAddress_id()).isEqualTo(id);

        // VERIFY DELETED
        Address afterDelete = Address.getBy("address_id", id);

        assertThat(afterDelete)
                .as("Address should not exist after deletion")
                .isNull();
    }
}