package es.roomie.user.mapper;

import es.roomie.user.model.TaskHistory;
import es.roomie.user.model.response.TaskHistoryResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

/**
 * Mapper interface for converting TaskHistory objects to TaskHistoryResponse objects.
 * Utilizes MapStruct for automatic mapping generation.
 */
@Mapper(componentModel = "spring")
public interface TaskHistoryMapper {

    /**
     * Maps a single TaskHistory object to a TaskHistoryResponse object.
     *
     * @param taskHistory the TaskHistory object to be mapped
     * @return the mapped TaskHistoryResponse object
     */
    TaskHistoryResponse mapTaskHistoryResponse(TaskHistory taskHistory);

    /**
     * Maps a list of TaskHistory objects to a list of TaskHistoryResponse objects.
     *
     * @param taskHistory the list of TaskHistory objects to be mapped
     * @return the list of mapped TaskHistoryResponse objects
     */
    @IterableMapping(
            elementTargetType = TaskHistoryResponse.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<TaskHistoryResponse> mapTaskHistoryResponse(List<TaskHistory> taskHistory);
}
