package org.example.gorest.controller;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.gorest.HttpRequest;
import org.example.gorest.endPoint.EndPoint;
import org.example.gorest.models.User;

public class UserController extends HttpRequest {

    public UserController(String url) {
        super(url);
    }

    @Step("Get all users")
    public User[] getAllUsers() {
        return super.get(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS)).as(User[].class);
    }

    @Step("Create new user")
    public User createNewUser(User user) {
        return super.post(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS), user.toJson()).as(User.class);
    }

    @Step("Get single user by id")
    public User getSingleUserById(Integer id) {
        return super.get(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS, String.valueOf(id)))
                .as(User.class);
    }

    @Step("PATCH user by id")
    public User partialUpdateUserDetailById(Integer id, User user) {
        Response response = super.patch(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS, String.valueOf(id)),
                user.toJson());
        if (response.getStatusCode() != 200) {
            System.out.println("ERROR: " + response.asPrettyString());
            throw new RuntimeException("Update failed");
        }
        return response.as(User.class);
    }

    @Step("Update all user details")
    public User updateAllUserDetails(Integer id, User user) {
        return super.put(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS, String.valueOf(id)), user.toJson())
                .as(User.class);
    }

    @Step("Delete user")
    public void deleteUser(Integer id) {
        super.delete(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS, String.valueOf(id)));
    }
}
