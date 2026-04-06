package gorestTests.api;

import lombok.extern.slf4j.Slf4j;
import org.example.gorest.controller.CommentController;
import org.example.gorest.controller.PostController;
import org.example.gorest.controller.ToDoController;
import org.example.gorest.controller.UserController;
import org.example.gorest.file.ConfigurationManager;
import org.example.gorest.file.CsvUtils;
import org.example.gorest.models.Comment;
import org.example.gorest.models.Post;
import org.example.gorest.models.ToDo;
import org.example.gorest.models.User;
import org.example.gorest.testdata.RandomDataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@Tag("API")
@Slf4j
public class EndToEndTest {

    protected static UserController userController;
    protected static PostController postController;
    protected static CommentController commentController;
    protected static ToDoController toDoController;
    private static final String CSV_PATH = "src/test/resources/users_data.csv";

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

//    @BeforeAll
//    static void innitControllers() {
//        userController = new UserController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
//        postController = new PostController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
//        commentController = new CommentController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
//        toDoController = new ToDoController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
//    }

    @Test
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
    void exportAllUsersToCsv() {
        List<User> users = Arrays.asList(userController.getAllUsers());
        CsvUtils.writeUsersToCsv(users, CSV_PATH);
    }

    @Test
    void getRandomUserFromCsv() {
        // взять рандомного пользователя целиком
        User randomUser = CsvUtils.getRandomUserFromCsv(CSV_PATH);
        Integer randomId = randomUser.getId();

        // работаем с рандомным id
        User userById = userController.getSingleUserById(randomId);
        assertThat(userController.getResponse().statusCode()).isEqualTo(200);
        assertThat(userById.getId()).isEqualTo(randomId);
    }

    @Test
    void getAllUsersFromCsvAndPickRandom() {
        // взять всех и поработать со списком
        List<User> users = CsvUtils.readUsersFromCsv(CSV_PATH);

        // рандомный id
        Integer randomId = users.get(new Random().nextInt(users.size())).getId();

        // все email-ы
        List<String> emails = users.stream().map(User::getEmail).toList();

        // все id-шки
        List<Integer> ids = users.stream().map(User::getId).toList();

        log.info("All ids: {}", ids);
        log.info("Random id picked: {}", randomId);
    }
}
