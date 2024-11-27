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

/**
 * UserController handles requests related to user operations.
 * This includes user registration, retrieval, and preference updates.
 */
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Constructor for UserController.
     *
     * @param userService the user service used for operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users based on the provided email set.
     *
     * @param userEmails a set of user emails to filter users
     * @return ResponseEntity containing a list of UserResponse objects
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(required = true) Set<String> userEmails) {
        return userService.getAllUsers(userEmails);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return ResponseEntity containing the UserResponse object
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    /**
     * Registers a new user.
     *
     * @param userRequest the request object containing user details
     * @return ResponseEntity with status OK
     */
    @PostMapping
    public ResponseEntity<Void> registerNewUser(@RequestBody UserRequest userRequest) {
        userService.registerNewUser(userRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates a user's preferences.
     *
     * @param principal    the JWT principal of the authenticated user
     * @param userId      the ID of the user to update preferences for
     * @param taskPreferences the list of task preferences to update
     * @return ResponseEntity containing a list of TaskPreferenceResponse objects
     * @throws UnAuthorizeUserException if the authenticated user does not have permission
     */
    @PutMapping("/{userId}/preferences")
    public ResponseEntity<List<TaskPreferenceResponse>> updateUserPreferences(@AuthenticationPrincipal Jwt principal,
                                                                        @PathVariable String userId,
                                                                        @Valid @RequestBody List<TaskPreferenceRequest> taskPreferences){
        if(!principal.getSubject().equals(userId)) {
            throw new UnAuthorizeUserException("You do not have permissions to access this resource.");
        }
        return userService.updateUserPreferences(userId, taskPreferences);
    }

    /**
     * Updates a user's availability.
     *
     * @param principal    the JWT principal of the authenticated user
     * @param userId      the ID of the user to update availability for
     * @param availabilities the list of availability requests to update
     * @return ResponseEntity containing a list of AvailabilityResponse objects
     * @throws UnAuthorizeUserException if the authenticated user does not have permission
     */
    @PutMapping("/{userId}/availabilities")
    public ResponseEntity<List<AvailabilityResponse>> updateUserAvailability(@AuthenticationPrincipal Jwt principal,
                                                                              @PathVariable String userId,
                                                                              @Valid @RequestBody List<AvailabilityRequest> availabilities){
        if(!principal.getSubject().equals(userId)) {
            throw new UnAuthorizeUserException("You do not have permissions to access this resource.");
        }
        return userService.updateUserAvailability(userId, availabilities);
    }

    /**
     * Retrieves the task history for a specific user.
     *
     * @param userId the ID of the user to retrieve tasks for
     * @return ResponseEntity containing a list of TaskHistoryResponse objects
     */
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskHistoryResponse>> getUserTask(@PathVariable String userId) {
        return userService.getUserTask(userId);
    }
}
