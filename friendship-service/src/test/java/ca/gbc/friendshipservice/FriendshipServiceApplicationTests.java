package ca.gbc.friendshipservice;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.repository.FriendshipRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class FriendshipServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    private FriendshipRequest getFriendshipRequest() {
        return FriendshipRequest.builder()
                .userId1("SomeUserID")
                .userId2("SomeFriendID")
                .build();
    }

    private List<Friendship> getFriendshipList() {
        List<Friendship> friendships = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        Friendship friendship = Friendship.builder()
                .id(uuid.toString())
                .build();
        friendships.add(friendship);
        return friendships;
    }

    // ... (Similar utility methods for string conversion)

    @Test
    void createFriendship() throws Exception {
        FriendshipRequest friendshipRequest = getFriendshipRequest();
        String friendshipRequestJsonString = objectMapper.writeValueAsString(friendshipRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/friendship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(friendshipRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Assertions.assertTrue(friendshipRepository.findAll().size() > 0);
    }


}
