package es.roomie.household.controller;

import es.roomie.household.model.response.HouseholdNameResponse;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import es.roomie.household.service.HouseholdService;
import es.roomie.household.service.client.feign.TokenService;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HouseholdController handles HTTP requests related to households.
 */
@RestController
@RequestMapping("/api/v1/households")
public class HouseholdController {

    private final HouseholdService householdService;
    private final TokenService tokenService;

    /**
     * Constructor for HouseholdController.
     *
     * @param householdService the service used to handle household operations
     * @param tokenService the service used to handle token operations
     */
    public HouseholdController(HouseholdService householdService, TokenService tokenService) {
        this.householdService = householdService;
        this.tokenService = tokenService;
    }

    /**
     * Creates a new household.
     *
     * @param principal the authenticated user's JWT
     * @param homeRequest the request body containing household details
     * @return a ResponseEntity with the created HouseholdResponse
     */
    @PostMapping
    public ResponseEntity<HouseholdResponse> createHousehold(@AuthenticationPrincipal Jwt principal,
                                                             @Valid @RequestBody HouseholdRequest homeRequest) {
        String memberEmail = principal.getClaim("email");
        tokenService.setToken(principal.getTokenValue());
        return householdService.createHousehold(homeRequest);
    }

    /**
     * Retrieves a list of households for the authenticated user.
     *
     * @param principal the authenticated user's JWT
     * @return a ResponseEntity containing a list of HouseholdResponse
     */
    @GetMapping
    public ResponseEntity<List<HouseholdResponse>> getHouseholds(@AuthenticationPrincipal Jwt principal) {
        String memberEmail = principal.getClaim("email");
        tokenService.setToken(principal.getTokenValue());
        return householdService.getHouseholds(memberEmail);
    }

    /**
     * Updates the members of a household by household ID.
     *
     * @param principal the authenticated user's JWT
     * @param householdId the ID of the household to update
     * @param memberEmails the list of member emails to update
     * @return a ResponseEntity with the updated HouseholdResponse
     */
    @PutMapping("/{householdId}/members")
    public ResponseEntity<HouseholdResponse> updateMembersByHouseholdId(@AuthenticationPrincipal Jwt principal,
                                                                        @PathVariable String householdId,
                                                                        @RequestParam List<String> memberEmails) {
        String admimMemberId = principal.getSubject();
        tokenService.setToken(principal.getTokenValue());
        return householdService.updateMembersByHouseholdId(householdId, admimMemberId, memberEmails);
    }

    /**
     * Updates the name of a household.
     *
     * @param principal the authenticated user's JWT
     * @param householdId the ID of the household to update
     * @param name the new name for the household
     * @return a ResponseEntity with the updated HouseholdNameResponse
     * @throws BadRequestException if the name is null or empty
     */
    @PutMapping("/{householdId}")
    public ResponseEntity<HouseholdNameResponse> updateHouseholdName(@AuthenticationPrincipal Jwt principal,
                                                                     @PathVariable String householdId,
                                                                     @RequestParam(required = true) String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("household name cannot be empty");
        }

        String userId = principal.getSubject();
        return householdService.updateHouseholdName(householdId, userId, name);
    }

    /**
     * Accepts or declines a membership invitation for a household.
     *
     * @param principal the authenticated user's JWT
     * @param householdId the ID of the household
     * @param invitationAccepted true if the invitation is accepted, false otherwise
     * @return a ResponseEntity indicating the result of the operation
     */
    @PatchMapping("/{householdId}/member")
    public ResponseEntity<?> isMemberInvitationAccepted(@AuthenticationPrincipal Jwt principal,
                                                        @PathVariable String householdId,
                                                        @RequestParam(value = "invitation-accepted", required = true) boolean invitationAccepted) {
        String memberEmail = principal.getClaim("email");
        return householdService.isMemberInvitationAccepted(householdId, memberEmail, invitationAccepted);
    }

    /**
     * Deletes a member from a household by household ID and member email.
     *
     * @param principal the authenticated user's JWT
     * @param householdId the ID of the household
     * @param memberEmail the email of the member to delete
     * @return a ResponseEntity with the updated HouseholdResponse
     */
    @DeleteMapping("/{householdId}/members/{memberEmail}")
    public ResponseEntity<HouseholdResponse> deleteMemberByHouseholdId(@AuthenticationPrincipal Jwt principal,
                                                                       @PathVariable String householdId,
                                                                       @PathVariable String memberEmail) {
        String adminMemberEmail = principal.getClaim("email");
        tokenService.setToken(principal.getTokenValue());
        return  householdService.deleteMemberByHouseHold(householdId, adminMemberEmail, memberEmail);
    }

    /**
     * Deletes a household by its ID.
     *
     * @param principal the authenticated user's JWT
     * @param householdId the ID of the household to delete
     * @return a ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/{householdId}")
    public ResponseEntity<?> deleteHouseholdByID(@AuthenticationPrincipal Jwt principal,
                                                 @PathVariable String householdId) {
        String email = principal.getClaim("email");
        return householdService.deleteHouseholdById(householdId, email);
    }
}
