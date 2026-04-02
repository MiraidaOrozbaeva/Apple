package org.example.demoqa.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode

public class UserWebTables {
    String firstName;
    String lastName;
    String email;
    Integer age;
    Integer salary;
    String department;
}
