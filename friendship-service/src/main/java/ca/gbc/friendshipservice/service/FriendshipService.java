package ca.gbc.friendshipservice.service;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.dto.FriendshipResponse;

import java.util.List;

public interface FriendshipService {

    void createFriendship(FriendshipRequest friendshipRequest);

    String updateFriendship(String friendshipId, FriendshipRequest friendshipRequest);

    void deleteFriendship(String friendshipId);

    List<FriendshipResponse> getAllFriendships();

    FriendshipResponse getFriendshipDetails(String friendshipId);

}
