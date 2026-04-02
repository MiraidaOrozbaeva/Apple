package org.example.demoqa.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void main(String[] args) {
        User user1 = new User(1, "Ramis");
        User user2 = new User(1, "Ramis");
        System.out.println(user1.equals(user2));




    }


}
