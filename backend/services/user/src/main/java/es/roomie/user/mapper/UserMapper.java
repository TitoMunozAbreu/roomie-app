package es.roomie.user.mapper;

import es.roomie.user.model.User;
import es.roomie.user.model.response.UserResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * A mapper interface for converting User and UserRepresentation objects to UserResponse.
 * This interface uses MapStruct to generate the implementation for mapping between
 * these objects automatically at compile time.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a User object and a UserRepresentation object to a UserResponse object.
     *
     * @param user the User object containing user information
     * @param userRepresentation the UserRepresentation object containing additional user details
     * @return UserResponse object containing mapped fields from User and UserRepresentation
     */
    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.taskHistories", target = "taskHistories")
    @Mapping(source = "user.availabilities", target = "availabilities")
    @Mapping(source = "userRepresentation.firstName", target = "firstName", defaultExpression = "java(userRepresentation.getFirstName())")
    @Mapping(source = "userRepresentation.lastName", target = "lastName", defaultExpression = "java(userRepresentation.getLastName())")
    @Mapping(source = "userRepresentation.email", target = "email", defaultExpression = "java(userRepresentation.getEmail())")
    UserResponse mapToUserResponse(User user, UserRepresentation userRepresentation);
}