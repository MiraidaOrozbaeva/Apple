package gorestTests.api;

import org.assertj.core.api.Assertions;
import org.example.gorest.controller.CommentController;
import org.example.gorest.controller.PostController;
import org.example.gorest.controller.ToDoController;
import org.example.gorest.controller.UserController;
import org.example.utils.file.ConfigurationManager;
import org.example.gorest.models.Comment;
import org.example.gorest.models.Post;
import org.example.gorest.models.ToDo;
import org.example.gorest.models.User;
import org.example.gorest.testdata.RandomDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
@Tag("API")
public class UsersTest {

    UserController userController = new UserController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
    PostController postController = new PostController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
    CommentController commentController = new CommentController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
    ToDoController toDoController = new ToDoController(ConfigurationManager.getBaseConfig().gorestBaseUrl());
    private Integer userId;
    private Integer postId;

    @BeforeEach
    void setUp() {
        User user = RandomDataGenerator.createRandomUser();
        userId = userController.createNewUser(user).getId();
        Post post = RandomDataGenerator.createRandomPost();
        postId = postController.createUserPost(post, userId).getId();
        // postId создаётся аналогично
    }

    @AfterEach
    void tearDown() {
        userController.deleteUser(userId);
    }

    @Test
    void performFullCrudOperationsForUserTest() {
        userController.getAllUsers();

        User user = RandomDataGenerator.createRandomUser();
        User actualUser = userController.createNewUser(user);
        Integer id = actualUser.getId();
        Assertions.assertThat(userController.getResponse().getStatusCode())
                .as("Actual and expected status code are mismatch")
                .isEqualTo(201);

        Assertions.assertThat(actualUser)
                .as("Response body mismatch")
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(user);

        Assertions.assertThat(userController.getResponse().getTime())
                .as("More than 1 min")
                .isLessThan(3000);

        Assertions.assertThat(userController.getResponse().getHeader("x-frame-options"))
                .as("is not SAMEORIGIN")
                .isEqualTo("SAMEORIGIN");

        Assertions.assertThat(userController.getResponse().asByteArray().length)
                .as("Response size should be greater than 0")
                .isGreaterThan(0);

        Assertions.assertThat(actualUser.getId()).as("ID is NULL").isNotEqualTo(null);

        userController.getSingleUserById(id);

        User updatedUser = User.builder().name("NewNameUpdate").build();
        userController.partialUpdateUserDetailById(id, updatedUser);

        User userAllDetailsUpdated = RandomDataGenerator.createRandomUser();
        userController.updateAllUserDetails(id, userAllDetailsUpdated);

        userController.deleteUser(id);
    }

    @Test
    @Tag("SMOKE")
    void postTest() {
        System.out.println("GET ALL USER'S POSTS: " + Arrays.toString(postController.getAllUsersPosts()));

        System.out.println("GET POST BY USER ID: " + Arrays.toString(postController.getUserPostsById(userId)));

        Post post = RandomDataGenerator.createRandomPost();
        Post createdPost = postController.createUserPost(post, userId);
        System.out.println("CREATE USER POST: " + createdPost);

//        postController.deletePost(newIdishka);
    }

    @Test
    @Tag("SMOKE")
    void commentTest() {
        System.out.println("GET USER'S COMMENTS BY ID: " + Arrays.toString(commentController
                .getUserCommentsById(userId)));

        Comment comment = Comment.builder().name("Sandy Hand").email("olivia.murazik@yahoo.com")
                .body(RandomDataGenerator.randomBody()).build();
        Comment createdComment = commentController.createUserComments(comment, postId);
        System.out.println("CREATE POST COMMENT: " + createdComment);
    }

    @Test
    @Tag("SMOKE")
    void toDoTest() {
        System.out.println("GET USER'S TODOS BY ID: " + Arrays.toString(toDoController.getUserToDosById(userId)));

        ToDo toDo = ToDo.builder().title(RandomDataGenerator.randomTitle()).due_on("5:30am").status("pending").build();
        ToDo createdToDo = toDoController.createUserToDo(toDo, userId);
        System.out.println("CREATE POST COMMENT: " + createdToDo);
    }
}
