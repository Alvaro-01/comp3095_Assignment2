package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        log.info("Creating a new comment");

        Comment comment = Comment.builder()
                .text(commentRequest.getText())
                .userId(commentRequest.getUserId())
                .build();

        Comment savedComment = commentRepository.save(comment);
        log.info("Comment {} is saved", savedComment.getId());

        return mapToCommentResponse(savedComment);
    }

    @Override
    public CommentResponse getCommentById(String commentId) {
        log.info("Fetching comment by ID: {}", commentId);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.map(this::mapToCommentResponse).orElse(null);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        log.info("Returning a list of comments");

        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentRequest commentRequest) {
        log.info("Updating a comment with ID: {}", commentId);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setText(commentRequest.getText());
            comment.setUserId(commentRequest.getUserId());
            Comment updatedComment = commentRepository.save(comment);
            log.info("Comment {} is updated", updatedComment.getId());
            return mapToCommentResponse(updatedComment);
        }
        return null;
    }

    @Override
    public boolean deleteComment(String commentId) {
        log.info("Deleting comment with ID: {}", commentId);

        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            log.info("Comment {} is deleted", commentId);
            return true;
        }
        return false;
    }

    // Helper method to map Comment to CommentResponse
    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .userId(comment.getUserId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
