package org.example.gorest.testdata;

import com.github.javafaker.Faker;
import org.example.gorest.models.Post;
import org.example.gorest.models.User;

public class RandomDataGenerator {

    private static final Faker faker = new Faker();

    public static String randomName(){
        return faker.name().fullName();
    }

    public static String randomEmail(){
        return faker.internet().emailAddress();
    }

    public static String randomTitle(){
        return faker.harryPotter().book();
    }
    public static String randomBody(){
        return faker.harryPotter().quote();
    }

    public static User createRandomUser(){
        return User.builder().name(randomName()).email(randomEmail()).gender("female").status("active").build();
    }

    public static Post createRandomPost(){
        return Post.builder().title(randomTitle()).body(randomBody()).build();
    }
}
