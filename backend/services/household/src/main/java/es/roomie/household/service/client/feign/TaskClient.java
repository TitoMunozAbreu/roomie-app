package es.roomie.household.service.client.feign;

import es.roomie.household.model.feign.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "task-service",
        url = "${task.url}"
)
public interface TaskClient {

    @GetMapping
    ResponseEntity<List<TaskResponse>> getTasksByHouseholdIdIn(@RequestParam List<String> householdIds);
}
