package ca.gbc.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//Group23

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "user")
public class User {

    @Id
    private String id;
    private String name;

}