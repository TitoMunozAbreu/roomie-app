package es.roomie.household.model.response;

import com.netflix.spectator.api.Statistic;
import es.roomie.household.model.Member;
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
    private List<Member> members;
    private List<Task> tasks;
}
