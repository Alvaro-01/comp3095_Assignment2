package ca.gbc.userservice.controller;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.service.UserServiceimpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Group23

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceimpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequest userRequest){
        userService.createUser((userRequest));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping({"/{userId}"})
    public ResponseEntity<?>_updateUser(@PathVariable ("userId") String userId,
                                        @RequestBody UserRequest userRequest){
        String updateUserId = userService.updateUser(userId, userRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/user/" + updateUserId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
