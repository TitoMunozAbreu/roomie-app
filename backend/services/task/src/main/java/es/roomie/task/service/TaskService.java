package es.roomie.task.service;

import es.roomie.task.config.enums.Status;
import es.roomie.task.exceptions.ResourceNotFoundException;
import es.roomie.task.mapper.TaskMapper;
import es.roomie.task.model.Task;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public ResponseEntity<TaskResponse> createTask(TaskResquest taskResquest) {
        Task task = taskMapper.mapToTask(taskResquest);
        incrementTotalTask(task);

        log.info("Insert task");
        taskRepository.insert(task);

        return new ResponseEntity<>(taskMapper.mapToTaskResponse(task), OK);
    }

    public ResponseEntity<TaskResponse> updateTask(String taskId, TaskResquest taskResquest) {
        Task taskFound = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found."));

        taskMapper.updateTaskFromRequest(taskResquest, taskFound);

        if(taskFound.getStatus() == Status.Completed) {
            incrementCompletedTask(taskFound);
        }

        log.info("Update task");
        taskRepository.save(taskFound);

        return new ResponseEntity<>(taskMapper.mapToTaskResponse(taskFound), OK);
    }

    private Task incrementTotalTask(Task task) {
        task.getStatistics().incrementTotalTask();
        return task;
    }

    private void incrementCompletedTask(Task task) {
        task.getStatistics().incrementCompletedTasks();
    }
}