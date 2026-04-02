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

public class ToDo extends BaseModel{

    Integer id;
    Integer user_id;
    String title;
    String due_on;
    String status;
}
