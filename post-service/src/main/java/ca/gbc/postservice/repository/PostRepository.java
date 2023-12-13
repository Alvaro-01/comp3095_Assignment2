package ca.gbc.postservice.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import ca.gbc.postservice.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

    @DeleteQuery
    void deleteById(String _post_id);
}

