package org.example.demoqa.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    String firstName;
    int age;
    String lastName;
    String city;
    String gender;
    String subject;

    public static void main(String[] args) {
        Student student = Student.builder().firstName("Miraida").build();
        System.out.println(student);
    }
}

