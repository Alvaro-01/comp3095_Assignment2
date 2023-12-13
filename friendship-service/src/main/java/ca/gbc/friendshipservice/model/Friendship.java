package ca.gbc.friendshipservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "friendship")
public class Friendship {

    @Id
    private String id;
    private String userId1;
    private String userId2;
    private String status;

}

