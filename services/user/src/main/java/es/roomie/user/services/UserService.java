package es.roomie.user.services;

import es.roomie.user.exceptions.ResourceNotFoundException;
import es.roomie.user.mapper.TaskPreferenceMapper;
import es.roomie.user.mapper.UserMapper;
import es.roomie.user.model.User;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.response.TaskPreferenceResponse;
import es.roomie.user.model.response.UserResponse;
import es.roomie.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    public UserService(UserRepository userRepository,
                       KeycloakService keycloakService,
                       UserMapper userMapper,
                       TaskPreferenceMapper taskPreferenceMapper) {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
        this.userMapper = userMapper;
        this.taskPreferenceMapper = taskPreferenceMapper;
    }

    public ResponseEntity<UserResponse> getUserById(String userId) {
        log.info("Fetch userInfo ID {}", userId);
        UserRepresentation userRepresentation = keycloakService.getUserById(userId);

        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserResponse userResponse = userMapper.mapToUserResponse(userFound,userRepresentation);
        return new ResponseEntity<>(userResponse, OK);
    }

    public ResponseEntity<List<TaskPreferenceResponse>> updateUserPreferences(String userId, List<TaskPreferenceRequest> taskPreferences) {
        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("Update user preferences");
        userFound.setTaskPreferences(taskPreferenceMapper.mapToTaskPreference(taskPreferences));
        userRepository.save(userFound);
        List<TaskPreferenceResponse> taskPreferenceResponses = taskPreferenceMapper.mapToTaskPreferenceResponse(userFound.getTaskPreferences());
        return new ResponseEntity<>(taskPreferenceResponses, ACCEPTED);
    }
}
