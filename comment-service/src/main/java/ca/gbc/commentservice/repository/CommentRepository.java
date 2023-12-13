package ca.gbc.commentservice.repository;

import ca.gbc.commentservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByUserId(String userId);

    @Query("{ 'userId' : ?0 }")
    void deleteByUserId(String userId);
}
