package es.roomie.household.service;

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

import static es.roomie.household.config.enums.Role.admin;
import static es.roomie.household.config.enums.Role.member;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional(rollbackFor = {ResourceNotFoundException.class, ForbiddenUserException.class})
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

        return new ResponseEntity<>(householdMapper.mapToHouseHoldResponse(newHousehold), CREATED);
    }

    public ResponseEntity<List<HouseholdResponse>> getHouseholds(String userId) {
        log.info("Fetch households");
        List<Household> houseHoldsByMemberUserId = householdRepository.findByMembersUserId(userId);

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
        Household householdFound = householdRepository.findByIdAndMembersUserIdAndMembersRole(householdId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found for the specified user."));

        List<Member> nonAdminMembers = householdFound.getMembers().stream()
                .filter(member -> !member.getRole().equals(admin))
                .toList();

        List<Member> removedMembers = nonAdminMembers.stream()
                .filter(member -> !memberIds.contains(member.getUserId()))
                .toList();

        householdFound.getMembers().removeIf(removedMembers::contains);

        List<Member> newMembers = memberIds.stream()
                .filter(id -> householdFound.getMembers().stream().noneMatch(currentMember -> currentMember.getUserId().equals(id)))
                .map(id -> Member.builder()
                        .userId(id)
                        .role(member)
                        .invitationAccepted(false)
                        .build())
                .toList();

        householdFound.getMembers().addAll(newMembers);

        householdRepository.save(householdFound);

        //TODO: send message to notification-service "User added to a household"
        //TODO: send message to notification-service "User deleted from household"
        log.info("Sent notification to users...");

        return new ResponseEntity<>(householdMapper.mapToHouseHoldResponse(householdFound), OK);
    }

    public ResponseEntity<HouseholdResponse> deleteMemberByHouseHold(String householdId, String userId, String memberId) {
        Household householdFound = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found."));

        householdFound.getMembers().stream()
                .filter(member -> member.getUserId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No such member found."));

        boolean isMemberAdmin = isMemberAdmin(userId, householdFound);

        boolean isSelfRemoval = userId.equals(memberId);

        if (!isMemberAdmin && !isSelfRemoval) {
            throw new ForbiddenUserException("You do not have permissions to access this resource.");
        }

        log.info("Delete household member");
        householdFound.getMembers()
                .removeIf(member -> member.getUserId().equals(memberId));

        householdRepository.save(householdFound);
        return new ResponseEntity<>(householdMapper.mapToHouseHoldResponse(householdFound), OK);
    }

    public ResponseEntity<?> deleteHouseholdById(String householdId, String userId) {
        Household householdFound = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found."));

        boolean isMemberAdmin = isMemberAdmin(userId, householdFound);

        if (!isMemberAdmin) {
            throw new ResourceNotFoundException("Member not found or lacks permissions to access this resource.");
        }

        householdRepository.delete(householdFound);

        //TODO: send request to task-service: "deleteTasksByHouseholdId"
        log.info("Sent request to task-service...");
        //TODO: send message to notification-service "deleted household"
        log.info("Sent notification to user...");

        return new ResponseEntity<>("Deleted household",OK);
    }

    private static boolean isMemberAdmin(String userId, Household householdFound) {
        return householdFound.getMembers().stream()
                .anyMatch(member -> member.getUserId().equals(userId) &&
                        member.getRole().equals(admin));
    }

}
