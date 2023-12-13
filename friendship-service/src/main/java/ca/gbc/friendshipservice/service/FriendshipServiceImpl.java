package ca.gbc.friendshipservice.service;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public abstract class FriendshipServiceImpl implements ca.gbc.friendshipservice.service.FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Override
    public void createFriendship(FriendshipRequest friendshipRequest) {
        log.info("Creating a new friendship");

        Friendship friendship = Friendship.builder()
                .userId1(friendshipRequest.getUserId1())
                .userId2(friendshipRequest.getUserId2())
                .status(friendshipRequest.getStatus())
                .build();

        friendshipRepository.save(friendship);
        log.info("Friendship between User {} and User {} is saved", friendship.getUserId1(), friendship.getUserId2());
    }

    @Override
    public String updateFriendship(String friendshipId, FriendshipRequest friendshipRequest) {
        log.info("Updating a friendship with id {}", friendshipId);

        Friendship friendship = friendshipRepository.findById(friendshipId).orElse(null);

        if (friendship != null) {
            friendship.setUserId1(friendshipRequest.getUserId1());
            friendship.setUserId2(friendshipRequest.getUserId2());
            friendship.setStatus(friendshipRequest.getStatus());
            friendshipRepository.save(friendship);
            log.info("Friendship {} is updated", friendship.getId());
        }
        return friendshipId;
    }

    @Override
    public void deleteFriendship(String friendshipId) {
        log.info("Deleting a friendship with id {}", friendshipId);
        friendshipRepository.deleteById(friendshipId);
    }

    @Override
    public List<FriendshipResponse> getAllFriendships() {
        log.info("Returning a list of Friendships");

        List<Friendship> friendships = friendshipRepository.findAll();
        return friendships.stream().map(this::mapToFriendshipResponse).toList();
    }

    private FriendshipResponse mapToFriendshipResponse(Friendship friendship) {
        return FriendshipResponse.builder()
                .friendshipId(friendship.getId())
                .userId1(friendship.getUserId1())
                .userId2(friendship.getUserId2())
                .status(friendship.getStatus())
                .build();
    }
}
