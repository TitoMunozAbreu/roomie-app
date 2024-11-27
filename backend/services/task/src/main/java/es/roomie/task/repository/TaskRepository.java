package es.roomie.task.repository;

import es.roomie.task.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Task entities.
 * This interface extends MongoRepository to provide CRUD operations for Task entities in the MongoDB database.
 */
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    /**
     * Finds all Task entities that belong to the specified household IDs.
     *
     * @param householdIds a list of household IDs to filter the tasks by
     * @return a list of Task entities associated with the specified household IDs
     */
    @Query("{'householdId': {'$in':  ?0 } }")
    List<Task> findByHouseholdIdIn(List<String> householdIds);

    /**
     * Deletes all Task entities associated with the specified household ID.
     *
     * @param householdId the household ID of the tasks to be deleted
     */
    void deleteByHouseholdId(String householdId);
}
