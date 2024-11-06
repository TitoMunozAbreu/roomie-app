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

@RestController
@RequestMapping("/api/v1/households")
public class HouseholdController {

    private final HouseholdService householdService;
    private final TokenService tokenService;

    public HouseholdController(HouseholdService householdService, TokenService tokenService) {
        this.householdService = householdService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<HouseholdResponse> createHousehold(@Valid @RequestBody HouseholdRequest homeRequest) {
        return householdService.createHousehold(homeRequest);
    }

    @GetMapping
    public ResponseEntity<List<HouseholdResponse>> getHouseholds(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getSubject();
        tokenService.setToken(principal.getTokenValue());
        return householdService.getHouseholds(userId);
    }

    @PutMapping("/{householdId}/members")
    public ResponseEntity<HouseholdResponse> updateMembersByHouseholdId(@AuthenticationPrincipal Jwt principal,
                                                                        @PathVariable String householdId,
                                                                        @RequestParam List<String> memberIds) {
        String userId = principal.getSubject();
        tokenService.setToken(principal.getTokenValue());
        return householdService.updateMembersByHouseholdId(householdId,userId, memberIds);
    }

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

    @DeleteMapping("/{householdId}/members/{memberId}")
    public ResponseEntity<HouseholdResponse> deleteMemberByHouseholdId(@AuthenticationPrincipal Jwt principal,
                                                                       @PathVariable String householdId,
                                                                       @PathVariable String memberId) {
        String userId = principal.getSubject();
        tokenService.setToken(principal.getTokenValue());
        return  householdService.deleteMemberByHouseHold(householdId, userId, memberId);
    }

    @DeleteMapping("/{householdId}")
    public ResponseEntity<?> deleteHouseholdByID(@AuthenticationPrincipal Jwt principal,
                                                 @PathVariable String householdId) {
        String userId = principal.getSubject();
        return householdService.deleteHouseholdById(householdId, userId);
    }
}
