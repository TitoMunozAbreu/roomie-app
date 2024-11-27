package es.roomie.household.service.client.feign;

import es.roomie.household.model.feign.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign client interface for interacting with the task service.
 * This interface defines methods for retrieving and deleting tasks
 * associated with household IDs.
 */
@FeignClient(
        name = "task-service",
        url = "${task.url}"
)
public interface TaskClient {

    /**
     * Retrieves a list of tasks based on the provided household IDs.
     *
     * @param householdIds A list of household IDs to filter the tasks.
     * @return A ResponseEntity containing a list of TaskResponse objects.
     */
    @GetMapping
    ResponseEntity<List<TaskResponse>> getTasksByHouseholdIdIn(@RequestParam List<String> householdIds);

    /**
     * Deletes a task associated with the specified household ID.
     *
     * @param householdId The ID of the household whose task is to be deleted.
     * @return A ResponseEntity indicating the result of the delete operation.
     */
    @DeleteMapping
    ResponseEntity<?> deleteTaskByHouseholdId(@RequestParam String householdId);
}
