package ca.gbc.userservice.service;
//Group23
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void createUser(UserRequest userRequest) {
        log.info("Creating a new user {}", userRequest.getName());

        User user = User.builder()
                .name(userRequest.getName())
                .build();

        userRepository.save(user);
        log.info("User {} is saved", user.getId());

    }



    @Override
    public String updateUser(String userId, UserRequest userRequest) {
        log.info("Updating a user with id {}", userId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        User user = mongoTemplate.findOne(query, User.class);

        if (user != null){
            user.setName(userRequest.getName());
            log.info("User {} is updated", user.getId());
            userRepository.save(user).getId();
        }
        return userId.toString();
    }


    @Override
    public void deleteUser(String userId) {
        log.info(" User {} is selected", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Returning a list a Users");

        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).toList();
    }

    private UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .Id(user.getId())
                .name(user.getName())
                .build();
    }


}