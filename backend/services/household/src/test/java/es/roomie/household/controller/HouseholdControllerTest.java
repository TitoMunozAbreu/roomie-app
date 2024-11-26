package es.roomie.household.controller;

import es.roomie.household.model.response.HouseholdNameResponse;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import es.roomie.household.service.HouseholdService;
import es.roomie.household.service.client.feign.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HouseholdControllerTest {

    @Mock
    private HouseholdService householdService;

    @Mock
    private TokenService tokenService;

    @Mock
    private Jwt principal;

    private HouseholdController householdController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        householdController = new HouseholdController(householdService, tokenService);
    }

    @Test
    void createHousehold_ValidRequest_ReturnsHouseholdResponse() {
        HouseholdRequest request = new HouseholdRequest("household", "userId", "test@mail.com");
        HouseholdResponse response = new HouseholdResponse();
        when(principal.getClaim("email")).thenReturn("test@example.com");
        when(principal.getTokenValue()).thenReturn("token");
        when(householdService.createHousehold(any(HouseholdRequest.class))).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<HouseholdResponse> result = householdController.createHousehold(principal, request);

        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(tokenService).setToken("token");
        verify(householdService).createHousehold(request);
    }

    @Test
    void getHouseholds_ValidRequest_ReturnsListOfHouseholdResponses() {
        List<HouseholdResponse> responses = Collections.singletonList(new HouseholdResponse());
        when(principal.getClaim("email")).thenReturn("test@example.com");
        when(principal.getTokenValue()).thenReturn("token");
        when(householdService.getHouseholds("test@example.com")).thenReturn(ResponseEntity.ok(responses));

        ResponseEntity<List<HouseholdResponse>> result = householdController.getHouseholds(principal);

        assertNotNull(result);
        assertEquals(responses, result.getBody());
        verify(tokenService).setToken("token");
        verify(householdService).getHouseholds("test@example.com");
    }

    @Test
    void updateMembersByHouseholdId_ValidRequest_ReturnsHouseholdResponse() {
        String householdId = "123";
        List<String> memberEmails = Collections.singletonList("member@example.com");
        HouseholdResponse response = new HouseholdResponse();
        when(principal.getSubject()).thenReturn("adminMemberId");
        when(principal.getTokenValue()).thenReturn("token");
        when(householdService.updateMembersByHouseholdId(householdId, "adminMemberId", memberEmails)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<HouseholdResponse> result = householdController.updateMembersByHouseholdId(principal, householdId, memberEmails);

        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(tokenService).setToken("token");
        verify(householdService).updateMembersByHouseholdId(householdId, "adminMemberId", memberEmails);
    }

    @Test
    void updateHouseholdName_ValidRequest_ReturnsHouseholdNameResponse() {
        String householdId = "123";
        String name = "New Household Name";
        HouseholdNameResponse response = new HouseholdNameResponse();
        when(principal.getSubject()).thenReturn("userId");
        when(householdService.updateHouseholdName(householdId, "userId", name)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<HouseholdNameResponse> result = householdController.updateHouseholdName(principal, householdId, name);

        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(householdService).updateHouseholdName(householdId, "userId", name);
    }


    @Test
    void isMemberInvitationAccepted_ValidRequest_ReturnsResponseEntity() {
        String householdId = "123";
        boolean invitationAccepted = true;
        when(principal.getClaim("email")).thenReturn("test@example.com");
        when(householdService.isMemberInvitationAccepted(householdId, "test@example.com", invitationAccepted))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> result = householdController.isMemberInvitationAccepted(principal, householdId, invitationAccepted);

        assertNotNull(result);
        verify(householdService).isMemberInvitationAccepted(householdId, "test@example.com", invitationAccepted);
    }

    @Test
    void deleteMemberByHouseholdId_ValidRequest_ReturnsHouseholdResponse() {
        String householdId = "123";
        String memberEmail = "member@example.com";
        HouseholdResponse response = new HouseholdResponse();
        when(principal.getClaim("email")).thenReturn("admin@example.com");
        when(principal.getTokenValue()).thenReturn("token");
        when(householdService.deleteMemberByHouseHold(householdId, "admin@example.com", memberEmail)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<HouseholdResponse> result = householdController.deleteMemberByHouseholdId(principal, householdId, memberEmail);

        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(tokenService).setToken("token");
        verify(householdService).deleteMemberByHouseHold(householdId, "admin@example.com", memberEmail);
    }

    @Test
    void deleteHouseholdByID_ValidRequest_ReturnsResponseEntity() {
        String householdId = "123";
        when(principal.getClaim("email")).thenReturn("test@example.com");
        when(householdService.deleteHouseholdById(householdId, "test@example.com")).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> result = householdController.deleteHouseholdByID(principal, householdId);

        assertNotNull(result);
        verify(householdService).deleteHouseholdById(householdId, "test@example.com");
    }
}