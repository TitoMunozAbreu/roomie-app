package es.roomie.user.mapper;

import es.roomie.user.model.TaskPreference;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.response.TaskPreferenceResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

/**
 * Mapper interface for converting between TaskPreference and its related request/response models.
 * This interface is used to define the mapping methods which will be implemented by MapStruct
 * to perform the conversions.
 */
@Mapper(componentModel = "spring")
public interface TaskPreferenceMapper {

    /**
     * Maps a TaskPreference entity to a TaskPreferenceResponse DTO.
     *
     * @param taskPreference the TaskPreference entity to map
     * @return the mapped TaskPreferenceResponse DTO
     */
    TaskPreferenceResponse mapToTaskPreferenceResponse(TaskPreference taskPreference);

    /**
     * Maps a TaskPreferenceRequest DTO to a TaskPreference entity.
     *
     * @param taskPreferenceRequest the TaskPreferenceRequest DTO to map
     * @return the mapped TaskPreference entity
     */
    TaskPreference mapToTaskPreference(TaskPreferenceRequest taskPreferenceRequest);

    /**
     * Maps a list of TaskPreference entities to a list of TaskPreferenceResponse DTOs.
     *
     * @param taskPreferences the list of TaskPreference entities to map
     * @return the list of mapped TaskPreferenceResponse DTOs
     */
    @IterableMapping(
            elementTargetType = TaskPreferenceResponse.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<TaskPreferenceResponse> mapToTaskPreferenceResponse(List<TaskPreference> taskPreferences);

    /**
     * Maps a list of TaskPreferenceRequest DTOs to a list of TaskPreference entities.
     *
     * @param taskPreferences the list of TaskPreferenceRequest DTOs to map
     * @return the list of mapped TaskPreference entities
     */
    @IterableMapping(
            elementTargetType = TaskPreference.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<TaskPreference> mapToTaskPreference(List<TaskPreferenceRequest> taskPreferences);
}