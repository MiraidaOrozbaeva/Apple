package gorestTests.api;

import lombok.extern.slf4j.Slf4j;
import org.example.gorest.controller.CommentController;
import org.example.gorest.controller.PostController;
import org.example.gorest.controller.ToDoController;
import org.example.gorest.controller.UserController;
import org.example.utils.file.ConfigurationManager;
import org.example.utils.file.CsvUtils;
import org.example.gorest.models.Comment;
import org.example.gorest.models.Post;
import org.example.gorest.models.ToDo;
import org.example.gorest.models.User;
import org.example.gorest.testdata.RandomDataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.Csv;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@Tag("API")
@Slf4j
public class EndToEndTest {

    protected static UserController userController;
    protected static PostController postController;
    protected static CommentController commentController;
    protected static ToDoController toDoController;
    private static final String USER_CSV = "src/test/resources/users_data.csv";

    @BeforeAll
    static void innitControllersCheck() {
        // Диагностика
        String url = ConfigurationManager.getBaseConfig().gorestBaseUrl();
        System.out.println("DEBUG gorest url: " + url); // если null - проблема в чтении файла

            userController = new UserController(url);
            postController = new PostController(url);
            commentController = new CommentController(url);
            toDoController = new ToDoController(url);
    }

    @Test
    @Tag("E2E")
    @DisplayName("E2E: create user → create post → create comment → create todo → delete user")
    void performFullUserWorkflowFromCreationToDeletion() {

        User user = RandomDataGenerator.createRandomUser();
        User createdUser = userController.createNewUser(user);
        Integer id = createdUser.getId();

        Post post = RandomDataGenerator.createRandomPost();
        Post createdPost = postController.createUserPost(post, id);
        Integer post_id = createdPost.getId();

        Comment comment = Comment.builder()
                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .body(RandomDataGenerator.randomBody())
                .build();
        commentController.createUserComments(comment, post_id);

        ToDo toDo = ToDo.builder()
                .title(RandomDataGenerator.randomTitle())
                .due_on("10:00 am")
                .status("pending").build();
        toDoController.createUserToDo(toDo, id);
    }

    @Test
    @Tag("SMOKE")
    @DisplayName("Export all users to csv file")
    void exportAllUsersToCsv() {
        List<User> users = Arrays.asList(userController.getAllUsers());
        CsvUtils.writeToCsv(users, USER_CSV, User.class);
    }

    @Test
    @Tag("SMOKE")
    @DisplayName("GET user by random id from CSV should return 200 and correct user data")
    void getRandomUserFromCsv() {
        // взять рандомного пользователя целиком
        Map<String, String> randomUser = CsvUtils.getRandomRow(USER_CSV);
        String name = randomUser.get("name");
        String randomId = CsvUtils.getRandomValue(USER_CSV, "id");
        int userId = Integer.parseInt(randomId);
        System.out.println("Random user: " + name);

        // работаем с рандомным id
        User userById = userController.getSingleUserById(userId);
        assertThat(userController.getResponse().statusCode()).isEqualTo(200);
        assertThat(userById.getId()).isEqualTo(userId);
    }

    @Test
    @Tag("SMOKE")
    @DisplayName("Get all ids and choose one randomly")
    void getAllUsersFromCsvAndPickRandom() {
        // взять всех и поработать со списком
        List<User> users = CsvUtils.readFromCsv(USER_CSV).stream()
                .map(row -> User.builder()
                        .id(Integer.parseInt(row.get("id")))
                        .name(row.get("name"))
                        .email(row.get("email"))
                        .gender(row.get("gender"))
                        .status(row.get("status"))
                        .build())
                .toList();

        User randomUser = users.get(new Random().nextInt(users.size()));

        // все email-ы
        List<String> emails = users.stream().map(User::getEmail).toList();

        // все id-шки
        List<Integer> ids = users.stream().map(User::getId).toList();

        log.info("All users: {}", users);
        log.info("All ids: {}", ids);
        log.info("All emails: {}", emails);
        log.info("Random user picked: {}", randomUser);
    }
}
