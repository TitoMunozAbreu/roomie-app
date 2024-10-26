package es.roomie.household.controller;

import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import es.roomie.household.service.HouseholdService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/households")
public class HouseholdController {

    private final HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @PostMapping
    public ResponseEntity<HouseholdResponse> createHousehold(@Valid @RequestBody HouseholdRequest homeRequest) {
        return householdService.createHousehold(homeRequest);
    }

    @GetMapping
    public ResponseEntity<List<HouseholdResponse>> getHouseholds(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getSubject();
        return householdService.getHouseholds(userId);
    }

    @PutMapping("/{householdId}/members")
    public ResponseEntity<HouseholdResponse> updateMembersByHouseholdId(@AuthenticationPrincipal Jwt principal,
                                                                        @PathVariable String householdId,
                                                                        @RequestParam List<String> memberIds) {
        String userId = principal.getSubject();
        return householdService.updateMembersByHouseholdId(householdId,userId, memberIds);
    }

    @DeleteMapping("/{householdId}/members/{memberId}")
    public ResponseEntity<HouseholdResponse> deleteMemberByHouseholdId(@AuthenticationPrincipal Jwt principal,
                                                                       @PathVariable String householdId,
                                                                       @PathVariable String memberId) {
        String userId = principal.getSubject();
        return  householdService.deleteMemberByHouseHold(householdId, userId, memberId);
    }
}
