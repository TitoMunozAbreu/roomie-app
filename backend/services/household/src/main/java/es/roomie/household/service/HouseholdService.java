package es.roomie.household.service;

import es.roomie.household.exceptions.ForbiddenUserException;
import es.roomie.household.exceptions.ResourceNotFoundException;
import es.roomie.household.mapper.HouseholdMapper;
import es.roomie.household.model.Household;
import es.roomie.household.model.Member;
import es.roomie.household.model.feign.UserResponse;
import es.roomie.household.model.response.HouseholdNameResponse;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.response.MemberResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import es.roomie.household.repository.HouseholdRepository;
import es.roomie.household.service.client.feign.UserClient;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final UserClient userClient;

    public HouseholdService(HouseholdMapper householdMapper, HouseholdRepository householdRepository, UserClient userClient) {
        this.householdMapper = householdMapper;
        this.householdRepository = householdRepository;
        this.userClient = userClient;
    }

    public ResponseEntity<HouseholdResponse> createHousehold(HouseholdRequest householRequest) {
        log.info("Create new household group");
        Household newHousehold = householdMapper.mapToHousehold(householRequest);

        householdRepository.insert(newHousehold);

        //TODO: send message to notification-service "new home created"
        log.info("Sent notification to user...");

        HouseholdResponse householdResponse = householdMapper.mapToHouseHoldResponse(newHousehold);

        getMembersDataFromHousehold(householdResponse);

        return new ResponseEntity<>(householdResponse, CREATED);
    }

    public ResponseEntity<List<HouseholdResponse>> getHouseholds(String memberEmail) {
        log.info("Fetch households");
        List<Household> houseHoldsByMemberUserId = householdRepository.findByMembersEmail(memberEmail);

        if (houseHoldsByMemberUserId.isEmpty()) {
            throw new ResourceNotFoundException("No households found.");
        }

        List<HouseholdResponse> householdResponses = householdMapper.mapToHouseholdResponse(houseHoldsByMemberUserId);

        getMembersDataFromHouseholds(householdResponses);

        //TODO: get task from task-service
        log.info("Get task by household from task-service...");

        return new ResponseEntity<>(householdResponses, OK);
    }

    public ResponseEntity<HouseholdResponse> updateMembersByHouseholdId(String householdId, String adminMemberId, List<String> memberEmails) {
        log.info("Fetch household");
        Household householdFound = householdRepository.findByIdAndMembersUserIdAndMembersRoleAdmin(householdId, adminMemberId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found for the specified user."));

        List<Member> nonAdminMembers = householdFound.getMembers().stream()
                .filter(member -> !member.getRole().equals(admin))
                .toList();

        List<Member> removedMembers = nonAdminMembers.stream()
                .filter(member -> !memberEmails.contains(member.getEmail()))
                .toList();

        householdFound.getMembers().removeIf(removedMembers::contains);

        List<Member> newMembers = memberEmails.stream()
                .filter(email -> householdFound.getMembers().stream().noneMatch(currentMember -> currentMember.getEmail().equals(email)))
                .map(email -> Member.builder()
                        .email(email)
                        .role(member)
                        .invitationAccepted(false)
                        .build())
                .toList();

        householdFound.getMembers().addAll(newMembers);

        householdRepository.save(householdFound);

        HouseholdResponse householdResponse = householdMapper.mapToHouseHoldResponse(householdFound);

        getMembersDataFromHousehold(householdResponse);

        //TODO: send message to notification-service "Accept Invitation to a household"
        //TODO: send message to notification-service "deleted from household"
        log.info("Sent notification to users...");

        return new ResponseEntity<>(householdResponse, OK);
    }

    public ResponseEntity<HouseholdResponse> deleteMemberByHouseHold(String householdId, String adminMemberEmail, String memberEmail) {
        Household householdFound = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found."));

        householdFound.getMembers().stream()
                .filter(member -> member.getEmail().equals(memberEmail))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No such member found."));

        boolean isMemberAdmin = isMemberAdmin(adminMemberEmail, householdFound);

        boolean isSelfRemoval = adminMemberEmail.equals(memberEmail);

        if (!isMemberAdmin && !isSelfRemoval) {
            throw new ForbiddenUserException("You do not have permissions to access this resource.");
        }

        log.info("Delete household member");
        householdFound.getMembers()
                .removeIf(member -> member.getEmail().equals(memberEmail));

        householdRepository.save(householdFound);

        HouseholdResponse householdResponse = householdMapper.mapToHouseHoldResponse(householdFound);

        getMembersDataFromHousehold(householdResponse);

        return new ResponseEntity<>(householdResponse, OK);
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

    private static boolean isMemberAdmin(String adminMemberEmail, Household householdFound) {
        return householdFound.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(adminMemberEmail) &&
                        member.getRole().equals(admin));
    }

    private List<HouseholdResponse> getMembersDataFromHouseholds(List<HouseholdResponse> householdResponses) {

        Set<String> membersEmails = householdResponses.stream()
                .flatMap(households -> households.getMembers().stream())
                .map(MemberResponse::getEmail)
                .collect(Collectors.toSet());

        List<UserResponse> users = new ArrayList<>();

        try {
            log.info("Fetch from user-service members info by emails {}", membersEmails);
            users = userClient.getUsers(membersEmails).getBody();
        } catch (RetryableException e) {
            log.error(e.getMessage());
        }

        if(users != null && !users.isEmpty()) {
            Map<String, UserResponse> userResponseMap = users.stream()
                    .collect(Collectors.toMap(UserResponse::email, Function.identity()));

            for( HouseholdResponse householdResponse : householdResponses) {

                List<MemberResponse> memberResponses = householdResponse.getMembers().stream()
                        .map(member -> {
                            UserResponse userResponse = userResponseMap.get(member.getEmail());
                            return HouseholdMapper.mapUsersToMemberResponse(member, userResponse);
                        })
                        .toList();

                householdResponse.setMembers(memberResponses);
            }
        }
        return householdResponses;
    }

    private HouseholdResponse getMembersDataFromHousehold(HouseholdResponse householdResponse) {

        Set<String> membersEmails = householdResponse.getMembers().stream()
                .map(MemberResponse::getEmail)
                .collect(Collectors.toSet());

        List<UserResponse> users = new ArrayList<>();

        try {
            log.info("Fetch from user-service members info by emails {}", membersEmails);
            users = userClient.getUsers(membersEmails).getBody();
        } catch (RetryableException e) {
            log.error(e.getMessage());
        }

        if(users != null && !users.isEmpty()) {
            Map<String, UserResponse> userResponseMap = users.stream()
                    .collect(Collectors.toMap(UserResponse::email, Function.identity()));

                List<MemberResponse> memberResponses = householdResponse.getMembers().stream()
                        .map(member -> {
                            UserResponse userResponse = userResponseMap.get(member.getEmail());
                            return HouseholdMapper.mapUsersToMemberResponse(member, userResponse);
                        })
                        .toList();

                householdResponse.setMembers(memberResponses);
        }
        return householdResponse;
    }

    public ResponseEntity<HouseholdNameResponse> updateHouseholdName(String householdId, String userId, String name) {
        Household household = householdRepository.findByIdAndMemberUserId(householdId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found or lacks permissions to access this resource."));

        log.info("Updating household name");
        household.setHouseholdName(name);
        householdRepository.save(household);

        return new ResponseEntity<>(householdMapper.mapToHouseholdNameResponse(household), OK);

    }
}
