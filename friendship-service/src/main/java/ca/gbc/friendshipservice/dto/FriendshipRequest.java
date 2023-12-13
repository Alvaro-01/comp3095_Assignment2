package ca.gbc.friendshipservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipRequest {

    private String userId1;
    private String userId2;
    private String status; // You can use this to set the initial status of the friendship request

}
