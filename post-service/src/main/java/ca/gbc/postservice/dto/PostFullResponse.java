package ca.gbc.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PostFullResponse {

    private String user_id;
    private String title;
    private String content;
    private String [] attachments;

}