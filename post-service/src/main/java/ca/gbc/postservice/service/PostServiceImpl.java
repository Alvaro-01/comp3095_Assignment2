package ca.gbc.postservice.service;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ca.gbc.postservice.dto.PostFullResponse;
import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository repository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void createPost(PostRequest postRequest) {
        log.info("Creating a new post:{}", postRequest.getTitle() );
        Post post = Post.builder()
                .user_id(postRequest.getUser_id())
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .attachments(postRequest.getAttachments())
                .build();

        repository.save(post);
        log.info("Post {} is saved", post.getId());
    }

    @Override
    public String updatePost(String postId, PostRequest postRequest) {
        log.info("Update post with ID: {}", postId);


        Query query = new Query().addCriteria(Criteria.where("id").is(postId));
        Post post = mongoTemplate.findOne(query, Post.class);

        if (post == null){
            post.setTitle(postRequest.getTitle());
            log.info("User {} is updated", post.getId());
            post.setContent(postRequest.getContent());
            repository.save(post).getId();
        }
        return postId.toString();

    }

    @Override
    public void deletePost(String _post_id) {
        log.info("Deleting post with ID: {}", _post_id);
        repository.deleteById(_post_id);
    }

    @Override
    public PostFullResponse getPost(String _post_id) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(_post_id));
        Post post = mongoTemplate.findOne(query, Post.class);
        if (post == null)
            return PostFullResponse.builder().build();
        return mapToPostFullResponse(post);
    }

    @Override
    public List<PostResponse> getPostsByUser(String _user_id) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(_user_id))
                .addCriteria(Criteria.where("deleted").is(false));
        return mongoTemplate.find(query, Post.class).stream().map(this::mapToPostResponse).toList();
    }

    private PostFullResponse mapToPostFullResponse(Post _post) {
        return PostFullResponse.builder()
                .user_id(_post.getUser_id())
                .title(_post.getTitle())
                .content(_post.getContent())
                .attachments(_post.getAttachments())
                .build();
    }

    private PostResponse mapToPostResponse(Post _post) {
        return PostResponse.builder()
                .id(_post.getId())
                .user_id(_post.getUser_id())
                .title(_post.getTitle())
                .attachments(_post.getAttachments().length > 0)
                .build();
    }
}