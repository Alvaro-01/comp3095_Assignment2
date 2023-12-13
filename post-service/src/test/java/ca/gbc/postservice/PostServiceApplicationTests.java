package ca.gbc.postservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;

@SpringBootTest
@AutoConfigureMockMvc
class PostServiceApplicationTests extends AbstractContainerBaseTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository repository;


    private static String USER_ID = "";
    private static String API_URL = "/api/post";

    private PostRequest getPostRequestSample() {
        return PostRequest.builder()
                .user_id("")
                .title("Texting Title")
                .content("Texting Context 1")
                .attachments(new String[] { "test1.jpg", "test2.png" })
                .build();
    }

    private List<Post> getPostList(int _q) {
        List<Post> postList = new ArrayList<>();

        for (int i = 0; i < _q; i++) {
            Post post = Post.builder()
                    .id(UUID.randomUUID().toString())
                    .user_id(USER_ID)
                    .title(String.format("testing title ({})", i))
                    .content(String.format("testing content ({})", i))
                    .attachments(new String[] {})
                    .build();
            postList.add(post);
        }

        return postList;
    }



    private PostRequest getPostRequest(){
        return PostRequest.builder()
                .title("Kaarish")
                .build();
    }

    @Test
    void createPost() throws Exception {

        PostRequest postRequest = getPostRequest();
        String userRequestJsonString = objectMapper.writeValueAsString(getPostRequestSample());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //Assertions
        Assertions.assertTrue(repository.findAll().size() > 0);

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is("Kaarish"));
        List<Post> post = mongoTemplate.find(query, Post.class);
        Assertions.assertTrue(post.size() > 0);
    }

    @Test
    void getOnePost() throws Exception {
        Post post = getPostList(1).get(0);
        repository.save(post);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders
                .get(API_URL + "/" + post.getId())
                .accept(MediaType.APPLICATION_JSON));

        res.andExpect(MockMvcResultMatchers.status().isOk());
        res.andDo(MockMvcResultHandlers.print());

        MvcResult result = res.andReturn();
        String json_response = result.getResponse().getContentAsString();
        JsonNode json = new ObjectMapper().readTree(json_response);

        assertTrue(json.size() == 1);
    }

    @Test
    void getPostByUser() throws Exception {
        int test_size = 5;
        repository.saveAll(getPostList(test_size));

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders
                .get(API_URL + "/user/" + USER_ID)
                .accept(MediaType.APPLICATION_JSON));

        res.andExpect(MockMvcResultMatchers.status().isOk());
        res.andDo(MockMvcResultHandlers.print());

        MvcResult result = res.andReturn();
        String json_response = result.getResponse().getContentAsString();
        JsonNode json = new ObjectMapper().readTree(json_response);

        assertTrue(json.size() >= test_size);
    }


    @Test
    void updatePost() throws Exception {
        Post post = getPostList(1).get(0);
        repository.save(post);

        post.setContent(post.getContent());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(API_URL + "/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(post.getId()));
        Post db_post = mongoTemplate.findOne(query, Post.class);

        assertEquals(post.getContent(), db_post.getContent());
    }

    @Test
    void deletePost() throws Exception {
        Post post = getPostList(1).get(0);
        repository.save(post);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(API_URL + "/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(post.getId()));

        assertEquals(0, mongoTemplate.count(query, Post.class));
    }
}