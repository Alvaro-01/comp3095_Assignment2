package ca.gbc.commentservice.controller;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments") // Adjust the URL path if needed
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse createdComment = commentService.createComment(commentRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/comments/" + createdComment.getId());
        return new ResponseEntity<>(createdComment, headers, HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable String commentId) {
        CommentResponse comment = commentService.getCommentById(commentId);
        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable String commentId,
            @RequestBody CommentRequest commentRequest) {
        CommentResponse updatedComment = commentService.updateComment(commentId, commentRequest);
        if (updatedComment != null) {
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        boolean deleted = commentService.deleteComment(commentId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
