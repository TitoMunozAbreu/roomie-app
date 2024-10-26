package es.roomie.household.mapper;

import es.roomie.household.config.enums.Role;
import es.roomie.household.model.Household;
import es.roomie.household.model.Member;
import es.roomie.household.model.response.HouseholdResponse;
import es.roomie.household.model.resquest.HouseholdRequest;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface HouseholdMapper {


    Household mapToHousehold(HouseholdRequest householRequest);

    HouseholdResponse mapToHouseHoldResponse(Household household);

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
                    .role(Role.admin)
                    .invitationAccepted(true)
                    .build());
        }
    }
}
