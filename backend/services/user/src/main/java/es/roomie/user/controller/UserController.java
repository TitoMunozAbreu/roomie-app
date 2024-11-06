package es.roomie.user.controller;

import es.roomie.user.exceptions.UnAuthorizeUserException;
import es.roomie.user.model.request.AvailabilityRequest;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.request.UserRequest;
import es.roomie.user.model.response.AvailabilityResponse;
import es.roomie.user.model.response.TaskHistoryResponse;
import es.roomie.user.model.response.TaskPreferenceResponse;
import es.roomie.user.model.response.UserResponse;
import es.roomie.user.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(required = true) Set<String> userIds) {
        return userService.getAllUsers(userIds);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Void> registerNewUser(@RequestBody UserRequest userRequest) {
        userService.registerNewUser(userRequest.userId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/preferences")
    public ResponseEntity<List<TaskPreferenceResponse>> updateUserPreferences(@AuthenticationPrincipal Jwt principal,
                                                                        @PathVariable String userId,
                                                                        @Valid @RequestBody List<TaskPreferenceRequest> taskPreferences){
        if(!principal.getSubject().equals(userId)) {
            throw new UnAuthorizeUserException("You do not have permissions to access this resource.");
        }
        return userService.updateUserPreferences(userId, taskPreferences);
    }

    @PutMapping("/{userId}/availabilities")
    public ResponseEntity<List<AvailabilityResponse>> updateUserAvailability(@AuthenticationPrincipal Jwt principal,
                                                                              @PathVariable String userId,
                                                                              @Valid @RequestBody List<AvailabilityRequest> availabilities){
        if(!principal.getSubject().equals(userId)) {
            throw new UnAuthorizeUserException("You do not have permissions to access this resource.");
        }
        return userService.updateUserAvailability(userId, availabilities);
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskHistoryResponse>> getUserTask(@PathVariable String userId) {
        return userService.getUserTask(userId);
    }
}
