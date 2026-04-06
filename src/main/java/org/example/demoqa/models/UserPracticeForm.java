package org.example.demoqa.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor // constructor with all params
@Builder // builder для того чтобы передать только выборочные параметры а остльные builder проставит по умолчанию
@NoArgsConstructor // default constructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
//@Data
@FieldDefaults(level = AccessLevel.PRIVATE) // делает все поля приватными

public class UserPracticeForm {
     String firstName;
     String lastName;
     String email;
     String gender;
     String mobileNumber;
     String dateOfBirth;
     String subject;
     String currentAddress;
}
