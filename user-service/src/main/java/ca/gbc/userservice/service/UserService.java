package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import java.util.List;
//Group23
public interface UserService {

    void createUser(UserRequest userRequest);
    String updateUser(String userId, UserRequest userRequest);
    void deleteUser(String userId);
    List<UserResponse> getAllUsers();
}
