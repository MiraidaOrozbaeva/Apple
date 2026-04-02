package org.example.demoqa.utils;

import com.github.javafaker.Faker;
import org.example.demoqa.models.UserPracticeForm;
import org.example.demoqa.models.UserTextBox;
import org.example.demoqa.models.UserWebTables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static Faker faker = new Faker();

    public static UserPracticeForm generatePracticeFormUser(){
        UserPracticeForm userPracticeForm = new UserPracticeForm();
        List<String> allSubjectsList = List.of("physics", "chemistry", "biology", "history", "civics",
                "computer science", "accounting", "social studies", "maths", "arts", "english", "economics",
                "commerce", "hindi");

        userPracticeForm.setFirstName(faker.name().firstName());
        userPracticeForm.setLastName(faker.name().lastName());
        userPracticeForm.setEmail(faker.internet().emailAddress());
        userPracticeForm.setGender(faker.options().option("female", "male", "other"));
        userPracticeForm.setMobileNumber(String.valueOf(faker.number().randomNumber(10,true)));
        userPracticeForm.setDateOfBirth(faker.date().birthday().toString());
        userPracticeForm.setSubject(faker.options().option(allSubjectsList).get(new Random().nextInt(allSubjectsList.size())));
        userPracticeForm.setCurrentAddress(faker.address().fullAddress());
        return userPracticeForm;
    }

    public static UserTextBox generateTextBoxForm(){
        UserTextBox userTextBox = new UserTextBox();

        userTextBox.setName(faker.name().firstName());
        userTextBox.setEmail(faker.internet().emailAddress());
        userTextBox.setCurrentAddress(faker.address().fullAddress());
        userTextBox.setPermanentAddress(faker.address().fullAddress());

        return userTextBox;
    }

    public static UserWebTables generateWebTablesAddNewRecordForm(){
        UserWebTables userWebTables = new UserWebTables();

        userWebTables.setFirstName(faker.name().firstName());
        userWebTables.setLastName(faker.name().lastName());
        userWebTables.setEmail(faker.internet().emailAddress());
        userWebTables.setAge(faker.number().numberBetween(18,70));
        userWebTables.setSalary(faker.number().numberBetween(500,5000));
        userWebTables.setDepartment(faker.job().position().trim().toLowerCase());

        return userWebTables;
    }

//    public static void main(String[] args) {
//        UserPracticeForm userPracticeForm = generatePracticeFormUser();
//        System.out.println(userPracticeForm);
//    }
}
