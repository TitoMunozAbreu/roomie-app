package es.roomie.user.mapper;

import es.roomie.user.model.Availability;
import es.roomie.user.model.TaskPreference;
import es.roomie.user.model.request.AvailabilityRequest;
import es.roomie.user.model.response.AvailabilityResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

/**
 * Mapper interface for converting between Availability and its related response/request models.
 * This interface uses MapStruct to generate the implementation of the mapping methods.
 */
@Mapper(componentModel = "spring")
public interface AvailabilityMapper {

    /**
     * Maps an Availability object to an AvailabilityResponse object.
     *
     * @param availability the Availability object to be mapped
     * @return the corresponding AvailabilityResponse object
     */
    AvailabilityResponse mapToAvailabilityResponse(Availability availability);

    /**
     * Maps an AvailabilityRequest object to an Availability object.
     *
     * @param availability the AvailabilityRequest object to be mapped
     * @return the corresponding Availability object
     */
    Availability mapToAvailability(AvailabilityRequest availability);

    /**
     * Maps a list of Availability objects to a list of AvailabilityResponse objects.
     *
     * @param availabilities the list of Availability objects to be mapped
     * @return the list of corresponding AvailabilityResponse objects
     */
    @IterableMapping(
            elementTargetType = AvailabilityResponse.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<AvailabilityResponse> mapToAvailabilityResponse(List<Availability> availabilities);

    /**
     * Maps a list of AvailabilityRequest objects to a list of Availability objects.
     *
     * @param availabilities the list of AvailabilityRequest objects to be mapped
     * @return the list of corresponding Availability objects
     */
    @IterableMapping(
            elementTargetType = TaskPreference.class,
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    List<Availability> mapToAvailability(List<AvailabilityRequest> availabilities);
}