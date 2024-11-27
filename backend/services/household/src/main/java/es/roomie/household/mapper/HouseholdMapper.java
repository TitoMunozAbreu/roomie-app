package es.roomie.household.mapper;

import es.roomie.household.config.enums.Role;
import es.roomie.household.model.Household;
import es.roomie.household.model.Member;
import es.roomie.household.model.feign.UserResponse;
import es.roomie.household.model.response.HouseholdNameResponse;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.response.MemberResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Mapper interface for converting between Household and its related DTOs.
 * This interface uses MapStruct to generate implementation at compile-time.
 */
@Mapper(componentModel = "spring")
public interface HouseholdMapper {

    /**
     * Maps a HouseholdRequest to a Household entity.
     *
     * @param householRequest the HouseholdRequest to map
     * @return the mapped Household entity
     */
    Household mapToHousehold(HouseholdRequest householRequest);

    /**
     * Maps a Household entity to a HouseholdResponse DTO.
     *
     * @param household the Household entity to map
     * @return the mapped HouseholdResponse DTO
     */
    HouseholdResponse mapToHouseHoldResponse(Household household);

    /**
     * Maps a Household entity to a HouseholdNameResponse DTO.
     *
     * @param household the Household entity to map
     * @return the mapped HouseholdNameResponse DTO
     */
    HouseholdNameResponse mapToHouseholdNameResponse(Household household);

    /**
     * Maps a list of Household entities to a list of HouseholdResponse DTOs.
     *
     * @param households the list of Household entities to map
     * @return the list of mapped HouseholdResponse DTOs
     */
    @IterableMapping(elementTargetType = Household.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<HouseholdResponse> mapToHouseholdResponse(List<Household> households);

    /**
     * After mapping, assigns a default role and initializes members if none exist.
     *
     * @param household the Household entity to which to assign defaults
     * @param householdRequest the HouseholdRequest containing user information
     */
    @AfterMapping
    default void assignDefaultRole(@MappingTarget Household household, HouseholdRequest householdRequest) {
        household.setId(UUID.randomUUID().toString());
        String userID = householdRequest.userId();

        if (household.getMembers() == null || household.getMembers().isEmpty()) {
            ArrayList<Member> members = new ArrayList<>();
            household.setMembers(members);
            members.add(Member.builder()
                    .userId(userID)
                    .email(householdRequest.email())
                    .role(Role.admin)
                    .invitationAccepted(true)
                    .build());
        }
    }

    /**
     * Maps a UserResponse to a MemberResponse.
     *
     * @param member the MemberResponse to populate
     * @param userResponse the UserResponse containing user data
     * @return the populated MemberResponse
     */
    static MemberResponse mapUsersToMemberResponse(MemberResponse member, UserResponse userResponse) {
        if(userResponse != null) {
            member.setFirstName(userResponse.firstName());
            member.setLastName(userResponse.lastName());
            member.setEmail(userResponse.email());
        }
        return member;
    }
}
