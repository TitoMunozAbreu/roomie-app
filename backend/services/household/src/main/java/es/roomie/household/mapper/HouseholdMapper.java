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

@Mapper(componentModel = "spring")
public interface HouseholdMapper {


    Household mapToHousehold(HouseholdRequest householRequest);

    HouseholdResponse mapToHouseHoldResponse(Household household);

    HouseholdNameResponse mapToHouseholdNameResponse(Household household);

    @IterableMapping(elementTargetType = Household.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<HouseholdResponse> mapToHouseholdResponse(List<Household> households);

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

    static MemberResponse mapUsersToMemberResponse(MemberResponse member, UserResponse userResponse) {
        if(userResponse != null) {
            member.setFirstName(userResponse.firstName());
            member.setLastName(userResponse.lastName());
            member.setEmail(userResponse.email());
        }
        return member;
    }
}
