package es.roomie.household.service;

import es.roomie.household.config.enums.Role;
import es.roomie.household.exceptions.ForbiddenUserException;
import es.roomie.household.exceptions.ResourceNotFoundException;
import es.roomie.household.mapper.HouseholdMapper;
import es.roomie.household.model.Household;
import es.roomie.household.model.Member;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import es.roomie.household.repository.HouseholdRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static es.roomie.household.config.enums.Role.member;
import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
@Slf4j
public class HouseholdService {
    private final HouseholdMapper householdMapper;
    private final HouseholdRepository householdRepository;

    public HouseholdService(HouseholdMapper householdMapper, HouseholdRepository householdRepository) {
        this.householdMapper = householdMapper;
        this.householdRepository = householdRepository;
    }

    public ResponseEntity<HouseholdResponse> createHousehold(HouseholdRequest householRequest) {
        log.info("Create new household group");
        Household newHousehold = householdMapper.mapToHousehold(householRequest);
        householdRepository.insert(newHousehold);

        //TODO: send message to notification-service "new home created"
        log.info("Sent notification to user...");

        return new ResponseEntity<HouseholdResponse>(householdMapper.mapToHouseHoldResponse(newHousehold), CREATED);
    }

    public ResponseEntity<List<HouseholdResponse>> getHouseholds(String userId) {
        log.info("Fetch households");
        List<Household> houseHoldsByMemberUserId = householdRepository.findHouseHoldsByMemberUserId(userId);

        if (houseHoldsByMemberUserId.isEmpty()) {
            throw new ResourceNotFoundException("No households found for the specified user.");
        }

        List<HouseholdResponse> householdResponses = householdMapper.mapToHouseholdResponse(houseHoldsByMemberUserId);

        //TODO: get task from task-service
        log.info("Get task by household from task-service...");

        return new ResponseEntity<>(householdResponses, OK);
    }

    public ResponseEntity<HouseholdResponse> updateMembersByHouseholdId(String householdId, String userId, List<String> memberIds) {
        log.info("Fetch household");
        Household householdFound = householdRepository.findHouseHoldsByIdAndMemberIdRoleAdmin(householdId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found for the specified user."));

        log.info("Update household members");
        memberIds.forEach(memberId -> householdFound.getMembers()
                .add(Member.builder()
                        .userId(memberId)
                        .role(member)
                        .invitationAccepted(false)
                        .build())
        );
        householdRepository.save(householdFound);

        //TODO: send message to notification-service "User added to a household"
        log.info("Sent notification to user...");

        return new ResponseEntity<>(householdMapper.mapToHouseHoldResponse(householdFound), OK);
    }

    public ResponseEntity<HouseholdResponse> deleteMemberByHouseHold(String householdId, String userId, String memberId) {
        Household householdFound = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found."));

        householdFound.getMembers().stream()
                .filter(member -> member.getUserId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No such member found."));

        boolean isMemberAdmin = householdFound.getMembers().stream()
                .anyMatch(member -> member.getUserId().equals(userId) &&
                        member.getRole().equals(Role.admin));

        boolean isSelfRemoval = userId.equals(memberId);

        if (!isMemberAdmin && !isSelfRemoval) {
            throw new ForbiddenUserException("You do not have permissions to access this resource.");
        }

        log.info("Delete household member");
        householdFound.getMembers()
                .removeIf(member -> member.getUserId().equals(userId));

        householdRepository.save(householdFound);
        return new ResponseEntity<>(householdMapper.mapToHouseHoldResponse(householdFound), OK);
    }
}
