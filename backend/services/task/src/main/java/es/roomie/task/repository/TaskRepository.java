package es.roomie.task.repository;

import es.roomie.task.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    @Query("{'householdId': {'$in':  ?0 } }")
    List<Task> findByHouseholdIdIn(List<String> householdIds);
}
