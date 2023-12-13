package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest commentRequest);
    CommentResponse getCommentById(String commentId);
    List<CommentResponse> getAllComments();
    CommentResponse updateComment(String commentId, CommentRequest commentRequest);
    boolean deleteComment(String commentId);
}
