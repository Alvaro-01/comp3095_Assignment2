package ca.gbc.postservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "post")

public class Post {

    @Id
    private String id;
    private String user_id;
    private String title;
    private String content;
    private String[] attachments;

}