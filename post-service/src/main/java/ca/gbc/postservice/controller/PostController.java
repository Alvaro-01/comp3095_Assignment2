package ca.gbc.postservice.controller;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ca.gbc.postservice.dto.PostFullResponse;
import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostServiceImpl postServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@RequestBody PostRequest _post) {
        postServiceImpl.createPost(_post);
    }

    @GetMapping({ "/{post_id}" })
    @ResponseStatus(HttpStatus.OK)
    public PostFullResponse getOnePost(@PathVariable("post_id") String _post_id) {
        return postServiceImpl.getPost(_post_id);
    }

    @GetMapping({ "/user/{user_id}" })
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> getPostByUser(@PathVariable("user_id") String _user_id) {
        return postServiceImpl.getPostsByUser(_user_id);
    }

    @PutMapping({ "/{post_id}" })
    public ResponseEntity<?> updatePost(@PathVariable("post_id") String _post_id,
                                        @RequestBody PostRequest _post) {
        String post_id = postServiceImpl.updatePost(_post_id, _post);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({ "/{post_id}" })
    public ResponseEntity<?> deletePost(@PathVariable("post_id") String _post_id) {
        postServiceImpl.deletePost(_post_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    public ResponseEntity<ObjectNode> errorHandler(Exception exception) {
        log.error("ERROR MESSAGE: ", exception);

        ObjectNode jsonObject = new ObjectMapper().createObjectNode();
        jsonObject.put("ERROR: ", exception.getMessage());

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<ObjectNode>(jsonObject, headers, 500);
    }
}