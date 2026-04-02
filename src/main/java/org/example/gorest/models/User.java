package org.example.gorest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@EqualsAndHashCode
@Setter
@Getter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class User extends BaseModel{

    Integer id;
    String name;
    String email;
    String gender;
    String status;
}
