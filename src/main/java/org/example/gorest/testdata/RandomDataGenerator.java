package org.example.gorest.testdata;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.example.gorest.models.Post;
import org.example.gorest.models.User;

public class RandomDataGenerator {

    private static final Faker faker = new Faker();

    @Step("Generate random full name")
    public static String randomName(){
        return faker.name().fullName();
    }

    @Step("Generate random email address")
    public static String randomEmail(){
        return faker.internet().emailAddress();
    }

    @Step("Generate random title")
    public static String randomTitle(){
        return faker.harryPotter().book();
    }

    @Step("Generate random body")
    public static String randomBody(){
        return faker.harryPotter().quote();
    }

    @Step("Generate random user")
    public static User createRandomUser(){
        return User.builder().name(randomName()).email(randomEmail()).gender("female").status("active").build();
    }

    @Step("Generate random post")
    public static Post createRandomPost(){
        return Post.builder().title(randomTitle()).body(randomBody()).build();
    }
}
