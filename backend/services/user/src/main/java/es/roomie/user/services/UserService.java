package es.roomie.user.services;

import es.roomie.user.exceptions.ResourceNotFoundException;
import es.roomie.user.mapper.AvailabilityMapper;
import es.roomie.user.mapper.TaskHistoryMapper;
import es.roomie.user.mapper.TaskPreferenceMapper;
import es.roomie.user.mapper.UserMapper;
import es.roomie.user.model.User;
import es.roomie.user.model.request.AvailabilityRequest;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.response.AvailabilityResponse;
import es.roomie.user.model.response.TaskHistoryResponse;
import es.roomie.user.model.response.TaskPreferenceResponse;
import es.roomie.user.model.response.UserResponse;
import es.roomie.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;
    private final TaskPreferenceMapper taskPreferenceMapper;
    private final AvailabilityMapper availabilityMapper;
    private final TaskHistoryMapper taskHistoryMapper;


    public UserService(UserRepository userRepository,
                       KeycloakService keycloakService,
                       UserMapper userMapper,
                       TaskPreferenceMapper taskPreferenceMapper,
                       AvailabilityMapper availabilityMapper,
                       TaskHistoryMapper taskHistoryMapper) {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
        this.userMapper = userMapper;
        this.taskPreferenceMapper = taskPreferenceMapper;
        this.availabilityMapper = availabilityMapper;
        this.taskHistoryMapper = taskHistoryMapper;
    }

    public ResponseEntity<UserResponse> getUserById(String userId) {
        log.info("Fetch userInfo ID {}", userId);
        UserRepresentation userRepresentation = keycloakService.getUserById(userId);

        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        UserResponse userResponse = userMapper.mapToUserResponse(userFound,userRepresentation);
        return new ResponseEntity<>(userResponse, OK);
    }

    public ResponseEntity<List<TaskPreferenceResponse>> updateUserPreferences(String userId, List<TaskPreferenceRequest> taskPreferences) {
        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        log.info("Update user preferences");
        userFound.setTaskPreferences(taskPreferenceMapper.mapToTaskPreference(taskPreferences));
        userRepository.save(userFound);
        List<TaskPreferenceResponse> taskPreferenceResponses = taskPreferenceMapper.mapToTaskPreferenceResponse(userFound.getTaskPreferences());
        return new ResponseEntity<>(taskPreferenceResponses, ACCEPTED);
    }

    public ResponseEntity<List<AvailabilityResponse>> updateUserAvailailities(String userId, List<AvailabilityRequest> availabilities) {
        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        log.info("Update user availabilities");
        userFound.setAvailabilities(availabilityMapper.mapToAvailability(availabilities));
        userRepository.save(userFound);
        List<AvailabilityResponse> availabilityResponses = availabilityMapper.mapToAvailabilityResponse(userFound.getAvailabilities());
        return new ResponseEntity<>(availabilityResponses, ACCEPTED);
    }

    public ResponseEntity<List<TaskHistoryResponse>> getUserTask(String userId) {
        log.info("Fetch userTask ID {}", userId);

        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(userFound.getTaskHistories() == null || userFound.getTaskHistories().isEmpty()) {
            throw new ResourceNotFoundException("No task history found for the user.");
        }
        return new ResponseEntity<>(taskHistoryMapper.mapTaskHistoryResponse(userFound.getTaskHistories()), ACCEPTED);
    }

    public void registerNewUser(String userId) {
        log.info("Check if exists userId {}", userId);
        Optional<User> userFound = userRepository.findById(userId);
        if(userFound.isEmpty()){
            log.info("Register new user {}", userId);
            User newUser = User.builder().id(userId).build();
            userRepository.save(newUser);
        }
    }
}
