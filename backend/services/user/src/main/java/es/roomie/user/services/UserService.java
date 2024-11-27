package es.roomie.user.services;

import es.roomie.user.exceptions.ResourceNotFoundException;
import es.roomie.user.mapper.AvailabilityMapper;
import es.roomie.user.mapper.TaskHistoryMapper;
import es.roomie.user.mapper.TaskPreferenceMapper;
import es.roomie.user.mapper.UserMapper;
import es.roomie.user.model.User;
import es.roomie.user.model.request.AvailabilityRequest;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.request.UserRequest;
import es.roomie.user.model.response.AvailabilityResponse;
import es.roomie.user.model.response.TaskHistoryResponse;
import es.roomie.user.model.response.TaskPreferenceResponse;
import es.roomie.user.model.response.UserResponse;
import es.roomie.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

/**
 * UserService provides various services related to user management,
 * including fetching user information, updating user preferences,
 * and managing user availabilities.
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;
    private final TaskPreferenceMapper taskPreferenceMapper;
    private final AvailabilityMapper availabilityMapper;
    private final TaskHistoryMapper taskHistoryMapper;

    /**
     * Fetches all users by their emails.
     *
     * @param userEmails Set of user emails to search for
     * @return ResponseEntity containing a list of UserResponse objects
     * @throws ResourceNotFoundException if no users are found
     */
    public ResponseEntity<List<UserResponse>> getAllUsers(Set<String> userEmails) {
        log.info("Get all users by emails {}", userEmails);
        List<User> usersFound = userRepository.findUserIdsByEmails(userEmails);

        if (usersFound.isEmpty()) {throw new ResourceNotFoundException("Users not found");}

        List<String> userIds = usersFound.stream()
                .map(User::getId)
                .toList();

        List<UserResponse> users = userIds.stream()
                .map(keycloakService::getUserById)
                .map(userRepresentation -> UserResponse.builder()
                        .id(userRepresentation.getId())
                        .firstName(userRepresentation.getFirstName())
                        .lastName(userRepresentation.getLastName())
                        .email(userRepresentation.getEmail())
                        .build())
                .toList();

        return new ResponseEntity<>(users, OK);
    }

    /**
     * Fetches user information by user ID.
     *
     * @param userId ID of the user to fetch
     * @return ResponseEntity containing the UserResponse object
     * @throws ResourceNotFoundException if the user is not found
     */
    public ResponseEntity<UserResponse> getUserById(String userId) {
        log.info("Fetch userInfo ID {}", userId);
        UserRepresentation userRepresentation = keycloakService.getUserById(userId);

        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        UserResponse userResponse = userMapper.mapToUserResponse(userFound,userRepresentation);
        return new ResponseEntity<>(userResponse, OK);
    }

    /**
     * Updates the user's task preferences.
     *
     * @param userId ID of the user whose preferences are to be updated
     * @param taskPreferences List of TaskPreferenceRequest objects
     * @return ResponseEntity containing a list of TaskPreferenceResponse objects
     * @throws ResourceNotFoundException if the user is not found
     */
    public ResponseEntity<List<TaskPreferenceResponse>> updateUserPreferences(String userId, List<TaskPreferenceRequest> taskPreferences) {
        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        log.info("Update user preferences");
        userFound.setTaskPreferences(taskPreferenceMapper.mapToTaskPreference(taskPreferences));
        userRepository.save(userFound);
        List<TaskPreferenceResponse> taskPreferenceResponses = taskPreferenceMapper.mapToTaskPreferenceResponse(userFound.getTaskPreferences());
        return new ResponseEntity<>(taskPreferenceResponses, ACCEPTED);
    }

    /**
     * Updates the user's availability.
     *
     * @param userId ID of the user whose availability is to be updated
     * @param availabilities List of AvailabilityRequest objects
     * @return ResponseEntity containing a list of AvailabilityResponse objects
     * @throws ResourceNotFoundException if the user is not found
     */
    public ResponseEntity<List<AvailabilityResponse>> updateUserAvailability(String userId, List<AvailabilityRequest> availabilities) {
        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        log.info("Update user availabilities");
        userFound.setAvailabilities(availabilityMapper.mapToAvailability(availabilities));
        userRepository.save(userFound);
        List<AvailabilityResponse> availabilityResponses = availabilityMapper.mapToAvailabilityResponse(userFound.getAvailabilities());
        return new ResponseEntity<>(availabilityResponses, ACCEPTED);
    }

    /**
     * Fetches the user's task history.
     *
     * @param userId ID of the user whose task history is to be fetched
     * @return ResponseEntity containing a list of TaskHistoryResponse objects
     * @throws ResourceNotFoundException if the user is not found or has no task history
     */
    public ResponseEntity<List<TaskHistoryResponse>> getUserTask(String userId) {
        log.info("Fetch userTask ID {}", userId);

        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(userFound.getTaskHistories() == null || userFound.getTaskHistories().isEmpty()) {
            throw new ResourceNotFoundException("No task history found for the user.");
        }
        return new ResponseEntity<>(taskHistoryMapper.mapTaskHistoryResponse(userFound.getTaskHistories()), ACCEPTED);
    }

    /**
     * Registers a new user if they do not already exist.
     *
     * @param userRequest UserRequest object containing user details
     */
    public void registerNewUser(UserRequest userRequest) {
        log.info("Check if exists userId {}", userRequest.userId());
        Optional<User> userFound = userRepository.findById(userRequest.userId());
        if(userFound.isEmpty()){
            log.info("Register new user {}", userRequest.userId());
            User newUser = User.builder()
                    .id(userRequest.userId())
                    .email(userRequest.email())
                    .build();
            userRepository.save(newUser);
        }
    }
}
