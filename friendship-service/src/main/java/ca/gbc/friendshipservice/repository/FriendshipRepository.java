package ca.gbc.friendshipservice.repository;

import ca.gbc.friendshipservice.model.Friendship;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FriendshipRepository extends MongoRepository<Friendship, String> {
    @DeleteQuery
    void deleteById(String friendshipId);
}

