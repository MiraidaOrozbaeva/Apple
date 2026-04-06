package org.example.demoqa.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Employee {
    String firstName;
    String lastName;
    int age;
    String email;
    int salary;
    String department;
}
