package es.roomie.user.mapper;

import es.roomie.user.model.TaskHistory;
import es.roomie.user.model.response.TaskHistoryResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskHistoryMapper {

    TaskHistoryResponse mapTaskHistoryResponse(TaskHistory taskHistory);

    @IterableMapping(
            elementTargetType = TaskHistoryResponse.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<TaskHistoryResponse> mapTaskHistoryResponse(List<TaskHistory> taskHistory);
}
