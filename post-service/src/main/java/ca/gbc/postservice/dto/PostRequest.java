package ca.gbc.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PostRequest {

    private String user_id;
    private String title;
    private String content;
    @Builder.Default
    private String[]attachments = new String[0];
}