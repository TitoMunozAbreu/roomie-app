package es.roomie.user.service;

import es.roomie.user.exceptions.ResourceNotFoundException;
import es.roomie.user.mapper.AvailabilityMapper;
import es.roomie.user.mapper.TaskHistoryMapper;
import es.roomie.user.mapper.TaskPreferenceMapper;
import es.roomie.user.mapper.UserMapper;
import es.roomie.user.model.TaskHistory;
import es.roomie.user.model.User;
import es.roomie.user.model.request.AvailabilityRequest;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.request.UserRequest;
import es.roomie.user.model.response.AvailabilityResponse;
import es.roomie.user.model.response.TaskHistoryResponse;
import es.roomie.user.model.response.TaskPreferenceResponse;
import es.roomie.user.model.response.UserResponse;
import es.roomie.user.repositories.UserRepository;
import es.roomie.user.services.KeycloakService;
import es.roomie.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TaskPreferenceMapper taskPreferenceMapper;

    @Mock
    private AvailabilityMapper availabilityMapper;

    @Mock
    private TaskHistoryMapper taskHistoryMapper;

    @InjectMocks
    private UserService userService;

    @Mock
    private User user;
    private UserRepresentation userRepresentation;


    @BeforeEach
    void setUp() {
        user = mock(User.class);
        userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");
        userRepresentation.setFirstName("Test");
        userRepresentation.setLastName("User");
        userRepresentation.setEmail("test@example.com");
    }

    @Test
    void testGetAllUsers_Success() {
        Set<String> emails = new HashSet<>();
        emails.add("test@example.com");

        when(userRepository.findUserIdsByEmails(emails)).thenReturn(List.of(user));
        when(keycloakService.getUserById("1")).thenReturn(userRepresentation);

        ResponseEntity<List<UserResponse>> response = userService.getAllUsers(emails);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Test", response.getBody().get(0).getFirstName());
    }

    @Test
    void testGetAllUsers_EmptyResult() {
        Set<String> emails = new HashSet<>();
        emails.add("notfound@example.com");

        when(userRepository.findUserIdsByEmails(emails)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers(emails));
    }

    @Test
    void testGetUserById_Success() {
        when(keycloakService.getUserById("1")).thenReturn(userRepresentation);
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userMapper.mapToUserResponse(any(), any())).thenReturn(new UserResponse());

        ResponseEntity<UserResponse> response = userService.getUserById("1");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(keycloakService.getUserById("1")).thenReturn(userRepresentation);
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById("1"));
    }

    @Test
    void testUpdateUserPreferences_Success() {
        List<TaskPreferenceRequest> taskPreferences = new ArrayList<>();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(taskPreferenceMapper.mapToTaskPreference(anyList())).thenReturn(new ArrayList<>());
        when(userRepository.save(any())).thenReturn(user);
        when(taskPreferenceMapper.mapToTaskPreferenceResponse(anyList())).thenReturn(new ArrayList<>());

        ResponseEntity<List<TaskPreferenceResponse>> response = userService.updateUserPreferences("1", taskPreferences);

        assertEquals(202, response.getStatusCode().value());
    }

    @Test
    void testUpdateUserPreferences_UserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserPreferences("1", new ArrayList<>()));
    }

    @Test
    void testUpdateUserAvailability_Success() {
        List<AvailabilityRequest> availabilities = new ArrayList<>();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(availabilityMapper.mapToAvailability(anyList())).thenReturn(new ArrayList<>());
        when(userRepository.save(any())).thenReturn(user);
        when(availabilityMapper.mapToAvailabilityResponse(anyList())).thenReturn(new ArrayList<>());

        ResponseEntity<List<AvailabilityResponse>> response = userService.updateUserAvailability("1", availabilities);

        assertEquals(202, response.getStatusCode().value());
    }

    @Test
    void testUpdateUserAvailability_UserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserAvailability("1", new ArrayList<>()));
    }

    @Test
    void testGetUserTask_Success() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        List<TaskHistory> taskHistories = List.of(
                TaskHistory.builder().taskId("1").completedDate("26/11/2024").build()
        );
        when(user.getTaskHistories()).thenReturn(taskHistories);

        List<TaskHistoryResponse> taskHistoryResponses = List.of(
                new TaskHistoryResponse("1", "26/11/2024")
        );
        when(taskHistoryMapper.mapTaskHistoryResponse(taskHistories)).thenReturn(taskHistoryResponses);

        ResponseEntity<List<TaskHistoryResponse>> response = userService.getUserTask("1");

        // Verifica la respuesta
        assertEquals(202, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("1", response.getBody().get(0).getTaskId());
    }

    @Test
    void testGetUserTask_UserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserTask("1"));
    }

    @Test
    void testGetUserTask_NoTaskHistory() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(user.getTaskHistories()).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserTask("1"));
    }

    @Test
    void testRegisterNewUser_UserDoesNotExist() {
        UserRequest userRequest = new UserRequest("1", "test@example.com");
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        userService.registerNewUser(userRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterNewUser_UserAlreadyExists() {
        UserRequest userRequest = new UserRequest("1", "test@example.com");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        userService.registerNewUser(userRequest);

        verify(userRepository, never()).save(any(User.class));
    }
}