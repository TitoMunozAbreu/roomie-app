package es.roomie.task.service;

import es.roomie.task.config.enums.Status;
import es.roomie.task.mapper.TaskMapper;
import es.roomie.task.model.Task;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    private Task incrementTotalTask(Task task) {
        task.getStatistics().incrementTotalTask();
        return task;
    }
}