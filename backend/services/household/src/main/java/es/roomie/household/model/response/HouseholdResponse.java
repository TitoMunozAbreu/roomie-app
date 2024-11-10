package es.roomie.household.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.roomie.household.model.response.feign.Task;
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
    private List<Task> tasks;
}
