package es.roomie.household.service;

import es.roomie.household.config.enums.Role;
import es.roomie.household.exceptions.ResourceNotFoundException;
import es.roomie.household.kafka.HouseholdProducer;
import es.roomie.household.kafka.NewMemberInvitation;
import es.roomie.household.kafka.NotificationMessage;
import es.roomie.household.mapper.HouseholdMapper;
import es.roomie.household.model.Household;
import es.roomie.household.model.Member;
import es.roomie.household.model.response.HouseholdNameResponse;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import es.roomie.household.repository.HouseholdRepository;
import es.roomie.household.service.client.feign.TaskClient;
import es.roomie.household.service.client.feign.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HouseholdServiceTest {

    @InjectMocks
    private HouseholdService householdService;

    @Mock
    private HouseholdMapper householdMapper;

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private TaskClient taskClient;

    @Mock
    private HouseholdProducer householdProducer;

    private HouseholdRequest householdRequest;
    private Household household;
    private HouseholdResponse householdResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        householdRequest = new HouseholdRequest("Test Household", "userId","test@example.com");
        Member adminMember = new Member("memberId","admin@example.com", Role.admin, true);
        household = new Household("1", "Test Household", Collections.singletonList(adminMember));
        householdResponse = new HouseholdResponse("1", "Test Household", Collections.emptyList(), null);
    }


    @Test
    void createHousehold_Failure_When_MappingError() {
        when(householdMapper.mapToHousehold(householdRequest)).thenThrow(new RuntimeException("Mapping error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            householdService.createHousehold(householdRequest);
        });

        assertEquals("Mapping error", exception.getMessage());
    }


    @Test
    void getHouseholds_Failure_When_NoHouseholdsFound() {
        when(householdRepository.findByMembersEmail("test@example.com")).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            householdService.getHouseholds("test@example.com");
        });

        assertEquals("No households found.", exception.getMessage());
    }


    @Test
    void updateMembersByHouseholdId_Failure_When_NoHouseholdFound() {
        when(householdRepository.findByIdAndMembersUserIdAndMembersRoleAdmin("1", "admin@example.com"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            householdService.updateMembersByHouseholdId("1", "admin@example.com", List.of("newmember@example.com"));
        });

        assertEquals("No household found for the specified user.", exception.getMessage());
    }


    @Test
    void updateHouseholdName_Failure_When_NoHouseholdFound() {
        when(householdRepository.findByIdAndMemberUserId("1", "user@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            householdService.updateHouseholdName("1", "user@example.com", "Updated Household");
        });

        assertEquals("Member not found or lacks permissions to access this resource.", exception.getMessage());
    }


    @Test
    void deleteMemberByHouseHold_Failure_When_NoHouseholdFound() {
        when(householdRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            householdService.deleteMemberByHouseHold("1", "admin@example.com", "member@example.com");
        });

        assertEquals("No household found.", exception.getMessage());
    }

    @Test
    void deleteHouseholdById_Success() {
        when(householdRepository.findById("1")).thenReturn(Optional.of(household));
        when(householdRepository.save(any())).thenReturn(household);

        ResponseEntity<?> response = householdService.deleteHouseholdById("1", "admin@example.com");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Deleted household", response.getBody());

        verify(householdRepository).delete(household);
        verify(householdProducer, times(1)).sendNotification(any(NotificationMessage.class));
    }

    @Test
    void deleteHouseholdById_Failure_When_NoHouseholdFound() {
        when(householdRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            householdService.deleteHouseholdById("1", "admin@example.com");
        });

        assertEquals("No household found.", exception.getMessage());
    }
}