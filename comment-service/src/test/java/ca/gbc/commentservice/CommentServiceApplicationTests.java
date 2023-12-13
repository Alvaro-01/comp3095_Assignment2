package ca.gbc.commentservice;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    private CommentRequest getCommentRequest() {
        return CommentRequest.builder()
                .text("This is a comment.")
                .userId("12345678")
                .build();
    }

    @Test
    void createComment() throws Exception {
        CommentRequest commentRequest = getCommentRequest();
        String commentRequestJsonString = objectMapper.writeValueAsString(commentRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Assertions
        List<Comment> comments = commentRepository.findAll();
        assertTrue(comments.size() > 0);
    }

    @Test
    void getAllComments() throws Exception {
        // Given
        Comment savedComment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Test comment")
                .userId("98765432")
                .build();

        commentRepository.save(savedComment);

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/comments")
                .accept(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        List<CommentResponse> commentResponses = objectMapper.readValue(jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, CommentResponse.class));

        assertEquals(1, commentResponses.size());
    }

    @Test
    void updateComment() throws Exception {
        // GIVEN
        Comment savedComment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Original comment")
                .userId("12345678")
                .build();

        commentRepository.save(savedComment);

        CommentRequest updatedCommentRequest = CommentRequest.builder()
                .text("Updated comment")
                .userId("87654321")
                .build();

        String updatedCommentRequestJsonString = objectMapper.writeValueAsString(updatedCommentRequest);

        // WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/comments/" + savedComment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedCommentRequestJsonString));

        // THEN
        response.andExpect(status().isNoContent());

        // Verify that the comment is updated in the database
        Comment updatedComment = commentRepository.findById(savedComment.getId()).orElse(null);
        assertNotNull(updatedComment);
        assertEquals(updatedCommentRequest.getText(), updatedComment.getText());
        assertEquals(updatedCommentRequest.getUserId(), updatedComment.getUserId());
    }

    @Test
    void deleteComment() throws Exception {
        // GIVEN
        Comment savedComment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Comment to delete")
                .userId("12345678")
                .build();

        commentRepository.save(savedComment);

        // WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/comments/" + savedComment.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // THEN
        response.andExpect(status().isNoContent());

        // Verify that the comment is deleted from the database
        Comment deletedComment = commentRepository.findById(savedComment.getId()).orElse(null);
        assertNull(deletedComment);
    }
}
