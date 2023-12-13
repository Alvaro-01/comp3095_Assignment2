package ca.gbc.postservice.service;

import java.time.LocalDate;
import java.util.List;

import ca.gbc.postservice.dto.PostFullResponse;
import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;

public interface PostService {
    void createPost(PostRequest _post);

    String updatePost(String _post_id, PostRequest _post);

    void deletePost(String _post_id);

    PostFullResponse getPost(String _post_id);

    List<PostResponse> getPostsByUser(String _user_id);

}