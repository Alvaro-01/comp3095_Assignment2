package ca.gbc.friendshipservice.controller;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createFriendship(@RequestBody FriendshipRequest friendshipRequest) {
        friendshipService.createFriendship(friendshipRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FriendshipResponse> getAllFriendships() {
        return friendshipService.getAllFriendships();
    }

    @GetMapping("/{friendshipId}")
    @ResponseStatus(HttpStatus.OK)
    public FriendshipResponse getFriendshipDetails(@PathVariable String friendshipId) {
        return friendshipService.getFriendshipDetails(friendshipId);
    }

    @PutMapping("/{friendshipId}")
    public ResponseEntity<?> updateFriendship(@PathVariable String friendshipId,
                                              @RequestBody FriendshipRequest friendshipRequest) {
        String updatedFriendshipId = friendshipService.updateFriendship(friendshipId, friendshipRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/friendships/" + updatedFriendshipId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<?> deleteFriendship(@PathVariable String friendshipId) {
        friendshipService.deleteFriendship(friendshipId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
