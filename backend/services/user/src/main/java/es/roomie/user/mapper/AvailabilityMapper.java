package es.roomie.user.mapper;

import es.roomie.user.model.Availability;
import es.roomie.user.model.TaskPreference;
import es.roomie.user.model.request.AvailabilityRequest;
import es.roomie.user.model.request.TaskPreferenceRequest;
import es.roomie.user.model.response.AvailabilityResponse;
import es.roomie.user.model.response.TaskPreferenceResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {
    AvailabilityResponse mapToAvailabilityResponse(Availability availability);
    Availability mapToAvailability(AvailabilityRequest availability);

    @IterableMapping(
            elementTargetType = AvailabilityResponse.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<AvailabilityResponse> mapToAvailabilityResponse(List<Availability> availabilities);

    @IterableMapping(
            elementTargetType = TaskPreference.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<Availability> mapToAvailability(List<AvailabilityRequest> availabilities);
}
