package es.roomie.household.model.response;

import es.roomie.household.model.feign.TaskResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseholdResponse {
    private String id;
    private String householdName;
    private List<MemberResponse> members;
    private List<TaskResponse> tasks;
}
