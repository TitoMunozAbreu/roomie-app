package es.roomie.household.service;

import es.roomie.household.exceptions.ForbiddenUserException;
import es.roomie.household.exceptions.ResourceNotFoundException;
import es.roomie.household.kafka.HouseholdProducer;
import es.roomie.household.kafka.NewMemberInvitation;
import es.roomie.household.kafka.NotificationMessage;
import es.roomie.household.mapper.HouseholdMapper;
import es.roomie.household.model.Household;
import es.roomie.household.model.Member;
import es.roomie.household.model.feign.TaskResponse;
import es.roomie.household.model.feign.UserResponse;
import es.roomie.household.model.response.HouseholdNameResponse;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.response.MemberResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import es.roomie.household.repository.HouseholdRepository;
import es.roomie.household.service.client.feign.TaskClient;
import es.roomie.household.service.client.feign.UserClient;
import feign.FeignException;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static es.roomie.household.config.enums.Role.admin;
import static es.roomie.household.config.enums.Role.member;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Service class responsible for managing household-related operations.
 * Contains methods for creating, retrieving, updating, and deleting households and their members.
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = {ResourceNotFoundException.class, ForbiddenUserException.class})
@Slf4j
public class HouseholdService {
    private final HouseholdMapper householdMapper;
    private final HouseholdRepository householdRepository;
    private final UserClient userClient;
    private final TaskClient taskClient;
    private final HouseholdProducer householdProducer;

    /**
     * Creates a new household based on the provided request data.
     *
     * @param householRequest The request containing the household data.
     * @return ResponseEntity containing the created HouseholdResponse object.
     */
    public ResponseEntity<HouseholdResponse> createHousehold(HouseholdRequest householRequest) {
        log.info("Create new household group");
        Household newHousehold = householdMapper.mapToHousehold(householRequest);

        householdRepository.insert(newHousehold);

        householdProducer.sendNotification(
                new NotificationMessage(
                        "New household created",
                        String.format("Congratulations! '%s' has been added to your household list.",
                                newHousehold.getHouseholdName()),
                        householRequest.email())
        );

        HouseholdResponse householdResponse = householdMapper.mapToHouseHoldResponse(newHousehold);

        getMembersDataFromHousehold(householdResponse);

        return new ResponseEntity<>(householdResponse, CREATED);
    }

    /**
     * Retrieves all households for a specific member identified by their email.
     *
     * @param memberEmail The email of the member whose households to fetch.
     * @return ResponseEntity containing a list of HouseholdResponse objects.
     */
    public ResponseEntity<List<HouseholdResponse>> getHouseholds(String memberEmail) {
        log.info("Fetch households");
        List<Household> houseHoldsByMemberUserId = householdRepository.findByMembersEmail(memberEmail);

        if (houseHoldsByMemberUserId.isEmpty()) {
            throw new ResourceNotFoundException("No households found.");
        }

        List<HouseholdResponse> householdResponses = householdMapper.mapToHouseholdResponse(houseHoldsByMemberUserId);

        getMembersDataFromHouseholds(householdResponses);

        getTasksDataFromHouseholdIds(householdResponses);

        return new ResponseEntity<>(householdResponses, OK);
    }

    /**
     * Updates the members of a household identified by its ID.
     *
     * @param householdId   The ID of the household to update.
     * @param adminMemberId The ID of the admin member performing the update.
     * @param memberEmails  The list of member emails to update.
     * @return ResponseEntity containing the updated HouseholdResponse object.
     */
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

        //Send notification to each member added
        newMembers.forEach(
                newMember -> {
                    householdProducer.sendNewMemberInvitation(
                            new NewMemberInvitation(
                                    "Welcome to the household",
                                    String.format("You have been added to the household '%s'.",
                                            householdFound.getHouseholdName()),
                                    newMember.getEmail(),
                                    String.format("http://localhost:5173/household/%s/acceptInvitation", householdFound.getId()))
                    );
                    log.info("Sent notification to member {}", newMember.getEmail());
                }
        );
        //Send notification to each member removed
        removedMembers.forEach(
                removedMember -> {
                    householdProducer.sendNotification(
                            new NotificationMessage(
                                    "Removed from household",
                                    String.format("You have been removed from the household '%s'.",
                                            householdFound.getHouseholdName()),
                                    removedMember.getEmail())
                    );
                    log.info("Sent notification to member {}", removedMember.getEmail());
                }
        );

        return new ResponseEntity<>(householdResponse, OK);
    }

    /**
     * Updates the name of a household identified by its ID.
     *
     * @param householdId The ID of the household to update.
     * @param userId      The ID of the user requesting the update.
     * @param name        The new name for the household.
     * @return ResponseEntity containing the updated HouseholdNameResponse object.
     */
    public ResponseEntity<HouseholdNameResponse> updateHouseholdName(String householdId, String userId, String name) {
        Household household = householdRepository.findByIdAndMemberUserId(householdId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found or lacks permissions to access this resource."));

        log.info("Updating household name");
        household.setHouseholdName(name);
        householdRepository.save(household);

        return new ResponseEntity<>(householdMapper.mapToHouseholdNameResponse(household), OK);

    }

    /**
     * Updates the invitation acceptance status of a member for a specific household.
     *
     * @param householdId        The ID of the household.
     * @param memberEmail        The email of the member to update.
     * @param invitationAccepted The new invitation status.
     * @return ResponseEntity containing a message indicating success or failure.
     */
    public ResponseEntity<?> isMemberInvitationAccepted(String householdId, String memberEmail, boolean invitationAccepted) {
        Household household = householdRepository.findByIdAndMemberEmail(householdId, memberEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found or lacks permissions to access this resource."));

        log.info("updating member invitation: {}", memberEmail);
        household.getMembers().stream()
                .filter(member -> member.getEmail().equals(memberEmail))
                .forEach(member -> member.setInvitationAccepted(invitationAccepted));

        householdRepository.save(household);
        Optional<String> adminMemberEmail = household.getMembers().stream()
                .filter(member -> Objects.equals(member.getRole(), admin))
                .findFirst()
                .map(Member::getEmail);

        //send notification the invatation response to admimMember
        if (adminMemberEmail.isPresent()) {
            if (invitationAccepted) {
                householdProducer.sendNotification(
                        new NotificationMessage(
                                "Invitation Accepted",
                                "'%s' has successfully joined the '%s' household.".formatted(memberEmail, household.getHouseholdName()),
                                adminMemberEmail.get()
                        )
                );
            } else {
                householdProducer.sendNotification(
                        new NotificationMessage(
                                "Invitation Declined",
                                "'%s' has declined to join the '%s' household.".formatted(memberEmail, household.getHouseholdName()),
                                adminMemberEmail.get()
                        )
                );
            }
        }
        String messageResponse = invitationAccepted ? "Invitation updated successfully." : "Invitation rejected.";

        return new ResponseEntity<>(messageResponse, OK);
    }

    /**
     * Deletes a member from a household.
     *
     * @param householdId      The ID of the household.
     * @param adminMemberEmail The email of the admin member requesting the deletion.
     * @param memberEmail      The email of the member to be deleted.
     * @return ResponseEntity containing the updated HouseholdResponse object.
     */
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

        householdProducer.sendNotification(
                new NotificationMessage(
                        "Removed from household",
                        String.format("You have been removed from the household '%s'.",
                                householdFound.getHouseholdName()),
                        memberEmail)
        );

        HouseholdResponse householdResponse = householdMapper.mapToHouseHoldResponse(householdFound);

        getMembersDataFromHousehold(householdResponse);

        return new ResponseEntity<>(householdResponse, OK);
    }

    /**
     * Deletes a household by its ID.
     *
     * @param householdId The ID of the household to delete.
     * @param userId      The ID of the user requesting the deletion.
     * @return ResponseEntity containing a message indicating successful deletion.
     */
    public ResponseEntity<?> deleteHouseholdById(String householdId, String userId) {
        Household householdFound = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("No household found."));

        boolean isMemberAdmin = isMemberAdmin(userId, householdFound);

        if (!isMemberAdmin) {
            throw new ResourceNotFoundException("Member not found or lacks permissions to access this resource.");
        }

        //delete task related to householdFound
        deleteTaskByHouseholdId(householdFound.getId());

        householdRepository.delete(householdFound);

        householdFound.getMembers().forEach(
                member -> {
                    householdProducer.sendNotification(
                            new NotificationMessage(
                                    "Household Removed",
                                    String.format("We regret to inform you that the household '%s' has been deleted. You are no longer a member of this household.",
                                            householdFound.getHouseholdName()),
                                    member.getEmail())
                    );
                }
        );

        return new ResponseEntity<>("Deleted household", OK);
    }

    /**
     * Checks if a member is an admin of the specified household.
     *
     * @param adminMemberEmail The email of the member to check.
     * @param householdFound   The household to check against.
     * @return True if the member is an admin, false otherwise.
     */
    private static boolean isMemberAdmin(String adminMemberEmail, Household householdFound) {
        return householdFound.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(adminMemberEmail) &&
                        member.getRole().equals(admin));
    }

    /**
     * Retrieves member data for a list of households.
     *
     * @param householdResponses The list of household responses to update.
     * @return Updated list of HouseholdResponse objects.
     */
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

        if (users != null && !users.isEmpty()) {
            Map<String, UserResponse> userResponseMap = users.stream()
                    .collect(toMap(UserResponse::email, Function.identity()));

            for (HouseholdResponse householdResponse : householdResponses) {

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

    /**
     * Retrieves member data for a single household.
     *
     * @param householdResponse The household response to update.
     * @return Updated HouseholdResponse object.
     */
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

        if (users != null && !users.isEmpty()) {
            Map<String, UserResponse> userResponseMap = users.stream()
                    .collect(toMap(UserResponse::email, Function.identity()));

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

    /**
     * Retrieves task data for a list of households based on their IDs.
     *
     * @param householdResponses The list of household responses to update with task data.
     * @return Updated list of HouseholdResponse objects.
     */
    private List<HouseholdResponse> getTasksDataFromHouseholdIds(List<HouseholdResponse> householdResponses) {

        List<String> householdIds = householdResponses.stream()
                .map(HouseholdResponse::getId)
                .toList();

        try {
            log.info("Fetch from task-service task info by householdIds {}", householdIds);
            //Group tasks by householdId
            Map<String, List<TaskResponse>> taskResponseMap = Objects.requireNonNull(taskClient.getTasksByHouseholdIdIn(householdIds)
                            .getBody())
                    .stream()
                    .collect(Collectors.groupingBy(TaskResponse::getHouseholdId));

            //assign tasks to each household
            householdResponses.forEach(household -> {
                List<TaskResponse> taskResponses = taskResponseMap.getOrDefault(household.getId(), Collections.emptyList());

                household.setTasks(taskResponses);
            });

        } catch (FeignException e) {
            log.error(e.getMessage());
        }
        return householdResponses;
    }

    /**
     * Deletes tasks related to a specified household.
     *
     * @param householdId The ID of the household whose tasks are to be deleted.
     */
    private void deleteTaskByHouseholdId(String householdId) {
        log.info("Request to task-service to delete task from household");

        try {
            taskClient.deleteTaskByHouseholdId(householdId);

        } catch (FeignException e) {
            log.warn(e.getMessage());
        }
    }
}
