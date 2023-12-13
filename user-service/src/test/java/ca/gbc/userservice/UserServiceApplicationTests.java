package ca.gbc.userservice;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.DirtiesContext;




import org.springframework.data.mongodb.core.query.Query;


//import javax.management.Query;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    private UserRequest getUserRequest(){
        return UserRequest.builder()
                .name("Kaarish")
                .build();
    }

    private List<User> getUserList() {

        List<User> users = new ArrayList<>();
        UUID uuid = UUID.randomUUID();

        User user = User.builder()
                .id(uuid.toString())
                .build();

        users.add(user);

        return users;

    }

    private String convertObjectToString(List<UserResponse> userList) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(userList);
    }

    private List<UserResponse> convertStringToObject(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<UserResponse>>() {
        });


    }


    @Test
    void createUser() throws Exception {

        UserRequest userRequest = getUserRequest();
        String userRequestJsonString = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //Assertions
        Assertions.assertTrue(userRepository.findAll().size() > 0);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Kaarish"));
        List<User> user = mongoTemplate.find(query, User.class);
        Assertions.assertTrue(user.size() > 0);
    }

    /******
     *  BOD - Behaviour Driven Development
     * //Given - Setup
     * // When - Action
     * // Then - Verify
     ***/
    //
    @Test
    void getAllUsers() throws Exception{

        //GIVEN
        userRepository.saveAll(getUserList());

        //WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.
                get("/api/user")
                .accept(MediaType.APPLICATION_JSON));

        //THEN
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getUserList().size();

        Assertions.assertEquals(expectedSize, actualSize);

    }

    @Test
    void updateUsers() throws Exception{

        //GIVEN
        User savedUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("King")
                .build();

        //Saved user with original name
        userRepository.save(savedUser);

        //prepare updated user and userRequest
        savedUser.setName("New Name");
        String userRequestString = objectMapper.writeValueAsString(savedUser);

        //WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/user/" + savedUser.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString));


        //THEN
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedUser.getId()));
        User storedUser = mongoTemplate.findOne(query, User.class);

        assertEquals(savedUser.getName(), storedUser.getName());
    }

    @Test
    void deleteUsers() throws Exception{

        //GIVEN
        User savedUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("King")
                .build();

        userRepository.save(savedUser);

        //WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/user/" + savedUser.getId().toString())
                .contentType(MediaType.APPLICATION_JSON));

        //THEN
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedUser.getId()));
        Long userCount = mongoTemplate.count(query, User.class);

        assertEquals(0, userCount);

    }

}