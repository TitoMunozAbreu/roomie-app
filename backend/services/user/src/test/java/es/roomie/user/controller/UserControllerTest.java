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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Jwt jwt;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        Set<String> userEmails = new HashSet<>();
        userEmails.add("test@example.com");

        List<UserResponse> userResponseList = new ArrayList<>();
        when(userService.getAllUsers(userEmails)).thenReturn(ResponseEntity.ok(userResponseList));

        ResponseEntity<List<UserResponse>> response = userController.getAllUsers(userEmails);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(userResponseList, response.getBody());
        verify(userService, times(1)).getAllUsers(userEmails);
    }

    @Test
    public void testGetUser() {
        String userId = "userId";
        UserResponse userResponse = new UserResponse();
        when(userService.getUserById(userId)).thenReturn(ResponseEntity.ok(userResponse));

        ResponseEntity<UserResponse> response = userController.getUser(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(userResponse, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testRegisterNewUser() {
        UserRequest userRequest = new UserRequest(
                "userId",
                "test@mail.com"
        );
        doNothing().when(userService).registerNewUser(userRequest);

        ResponseEntity<Void> response = userController.registerNewUser(userRequest);

        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).registerNewUser(userRequest);
    }

    @Test
    public void testUpdateUserPreferences_Success() {
        String userId = "userId";
        List<TaskPreferenceRequest> taskPreferences = new ArrayList<>();
        when(jwt.getSubject()).thenReturn(userId);
        List<TaskPreferenceResponse> taskPreferenceResponseList = new ArrayList<>();
        when(userService.updateUserPreferences(userId, taskPreferences)).thenReturn(ResponseEntity.ok(taskPreferenceResponseList));

        ResponseEntity<List<TaskPreferenceResponse>> response = userController.updateUserPreferences(jwt, userId, taskPreferences);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(taskPreferenceResponseList, response.getBody());
        verify(userService, times(1)).updateUserPreferences(userId, taskPreferences);
    }

    @Test
    public void testUpdateUserPreferences_Unauthorized() {
        String userId = "userId";
        List<TaskPreferenceRequest> taskPreferences = new ArrayList<>();
        when(jwt.getSubject()).thenReturn("differentUserId");

        UnAuthorizeUserException exception = assertThrows(UnAuthorizeUserException.class, () -> {
            userController.updateUserPreferences(jwt, userId, taskPreferences);
        });

        assertEquals("You do not have permissions to access this resource.", exception.getMessage());
    }

    @Test
    public void testUpdateUserAvailability_Success() {
        String userId = "userId";
        List<AvailabilityRequest> availabilities = new ArrayList<>();
        when(jwt.getSubject()).thenReturn(userId);
        List<AvailabilityResponse> availabilityResponseList = new ArrayList<>();
        when(userService.updateUserAvailability(userId, availabilities)).thenReturn(ResponseEntity.ok(availabilityResponseList));

        ResponseEntity<List<AvailabilityResponse>> response = userController.updateUserAvailability(jwt, userId, availabilities);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(availabilityResponseList, response.getBody());
        verify(userService, times(1)).updateUserAvailability(userId, availabilities);
    }

    @Test
    public void testUpdateUserAvailability_Unauthorized() {
        String userId = "userId";
        List<AvailabilityRequest> availabilities = new ArrayList<>();
        when(jwt.getSubject()).thenReturn("differentUserId");

        UnAuthorizeUserException exception = assertThrows(UnAuthorizeUserException.class, () -> {
            userController.updateUserAvailability(jwt, userId, availabilities);
        });

        assertEquals("You do not have permissions to access this resource.", exception.getMessage());
    }

    @Test
    public void testGetUserTask() {
        String userId = "userId";
        List<TaskHistoryResponse> taskHistoryResponseList = new ArrayList<>();
        when(userService.getUserTask(userId)).thenReturn(ResponseEntity.ok(taskHistoryResponseList));

        ResponseEntity<List<TaskHistoryResponse>> response = userController.getUserTask(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(taskHistoryResponseList, response.getBody());
        verify(userService, times(1)).getUserTask(userId);
    }
}