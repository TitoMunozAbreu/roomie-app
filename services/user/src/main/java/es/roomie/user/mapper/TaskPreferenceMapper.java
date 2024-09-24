package es.roomie.user.mapper;

import es.roomie.user.model.TaskPreference;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.response.TaskPreferenceResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskPreferenceMapper {

    TaskPreferenceResponse mapToTaskPreferenceResponse(TaskPreference taskPreference);
    TaskPreference mapToTaskPreference(TaskPreferenceRequest taskPreferenceRequest);

    @IterableMapping(
            elementTargetType = TaskPreferenceResponse.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<TaskPreferenceResponse> mapToTaskPreferenceResponse(List<TaskPreference> taskPreferences);

    @IterableMapping(
            elementTargetType = TaskPreference.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<TaskPreference> mapToTaskPreference(List<TaskPreferenceRequest> taskPreferences);
}
