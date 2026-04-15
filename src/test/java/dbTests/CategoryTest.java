package dbTests;

import org.example.db.beans.Category;
import org.example.db.db_utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("DB")
@DisplayName("Category DB tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryTest {

    private static Category createdCategory;

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseConnection.openConnection("dvdRental");
    }

    // ========================= GET ALL =========================

    @Test
    @Order(1)
    @Tag("SMOKE")
    @DisplayName("GET ALL: should return non-empty list of categories")
    void shouldReturnAllCategories() throws SQLException {
        List<Category> categories = Category.getAllFromCategory();

        assertThat(categories).isNotNull();
        assertThat(categories).isNotEmpty();

        Category first = categories.get(0);

        assertThat(first.getCategory_id()).isNotNull().isPositive();
        assertThat(first.getName()).isNotBlank();
        assertThat(first.getLast_update()).isNotNull();
    }

    // ========================= INSERT =========================

    @Test
    @Order(2)
    @Tag("SMOKE")
    @DisplayName("INSERT: should create new category and return it with generated id")
    void shouldInsertNewCategory() throws SQLException {

        Category categoryToInsert = Category.builder()
                .name("Test Category")
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        createdCategory = Category.insert(categoryToInsert);

        assertThat(createdCategory).isNotNull();
        assertThat(createdCategory.getCategory_id()).isNotNull().isPositive();
        assertThat(createdCategory.getName()).isEqualTo("Test Category");
        assertThat(createdCategory.getLast_update()).isNotNull();
    }

    // ========================= GET BY ID =========================

    @Test
    @Order(3)
    @Tag("SMOKE")
    @DisplayName("GET BY ID: should return correct category by id")
    void shouldGetCategoryById() throws SQLException {

        Category found = Category.getBy("category_id", createdCategory.getCategory_id());

        assertThat(found).isNotNull();
        assertThat(found.getCategory_id()).isEqualTo(createdCategory.getCategory_id());
        assertThat(found.getName()).isEqualTo(createdCategory.getName());
    }

    @Test
    @Order(4)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should return null for non-existing category")
    void shouldReturnNullForNonExistingCategory() throws SQLException {

        Category notFound = Category.getBy("category_id", 999999);

        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    @Tag("REGRESSION")
    @DisplayName("GET BY ID: should throw exception for invalid column")
    void shouldThrowExceptionForInvalidColumn() {

        assertThatThrownBy(() -> Category.getBy("invalid_column", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= UPDATE =========================

    @Test
    @Order(6)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update category name")
    void shouldUpdateCategoryName() throws SQLException {

        String newName = "Updated Category";

        Category updated = Category.update(createdCategory.getCategory_id(), "name", newName);

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(newName);
        assertThat(updated.getCategory_id()).isEqualTo(createdCategory.getCategory_id());
    }

    @Test
    @Order(7)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should update last_update field")
    void shouldUpdateLastUpdate() throws SQLException {

        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis());

        Category updated = Category.update(createdCategory.getCategory_id(), "last_update", newTimestamp);

        assertThat(updated.getLast_update()).isNotNull();
    }

    @Test
    @Order(8)
    @Tag("REGRESSION")
    @DisplayName("UPDATE: should throw exception for invalid column")
    void shouldThrowExceptionWhenUpdatingInvalidColumn() {

        assertThatThrownBy(() ->
                Category.update(createdCategory.getCategory_id(), "invalid", "value")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимая колонка");
    }

    // ========================= DELETE =========================

    @Test
    @Order(9)
    @Tag("SMOKE")
    @DisplayName("DELETE: should delete category and return deleted data")
    void shouldDeleteCategory() throws SQLException {

        Category deleted = Category.delete(createdCategory.getCategory_id());

        assertThat(deleted).isNotNull();
        assertThat(deleted.getCategory_id()).isEqualTo(createdCategory.getCategory_id());
        assertThat(deleted.getName()).isEqualTo("Updated Category");
    }

    @Test
    @Order(10)
    @Tag("REGRESSION")
    @DisplayName("DELETE: category should not exist after deletion")
    void shouldNotExistAfterDeletion() throws SQLException {

        Category afterDelete = Category.getBy("category_id", createdCategory.getCategory_id());

        assertThat(afterDelete).isNull();
    }

    @Test
    @Order(11)
    @Tag("REGRESSION")
    @DisplayName("DELETE: should return null for non-existing category")
    void shouldReturnNullWhenDeletingNonExisting() throws SQLException {

        Category deleted = Category.delete(999999);

        assertThat(deleted).isNull();
    }

    // ========================= E2E =========================

    @Test
    @Order(12)
    @Tag("E2E")
    @DisplayName("E2E: insert → get → update → delete category")
    void shouldPerformFullCategoryLifecycle() throws SQLException {

        // INSERT
        Category category = Category.builder()
                .name("E2E Category")
                .last_update(new Timestamp(System.currentTimeMillis()))
                .build();

        Category inserted = Category.insert(category);

        assertThat(inserted).isNotNull();
        assertThat(inserted.getCategory_id()).isPositive();

        int id = inserted.getCategory_id();

        // GET
        Category fetched = Category.getBy("category_id", id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getName()).isEqualTo("E2E Category");

        // UPDATE
        Category updated = Category.update(id, "name", "E2E Updated");

        assertThat(updated.getName()).isEqualTo("E2E Updated");

        // DELETE
        Category deleted = Category.delete(id);

        assertThat(deleted).isNotNull();

        // VERIFY
        Category afterDelete = Category.getBy("category_id", id);

        assertThat(afterDelete).isNull();
    }
}