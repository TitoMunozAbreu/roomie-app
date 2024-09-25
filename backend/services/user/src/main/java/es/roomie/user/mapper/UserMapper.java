package es.roomie.user.mapper;

import es.roomie.user.model.User;
import es.roomie.user.model.response.UserResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.taskHistories", target = "taskHistories")
    @Mapping(source = "user.availabilities", target = "availabilities")
    @Mapping(source = "userRepresentation.firstName", target = "firstName", defaultExpression = "java(userRepresentation.getFirstName())")
    @Mapping(source = "userRepresentation.lastName", target = "lastName", defaultExpression = "java(userRepresentation.getLastName())")
    @Mapping(source = "userRepresentation.email", target = "email", defaultExpression = "java(userRepresentation.getEmail())")
    UserResponse mapToUserResponse(User user, UserRepresentation userRepresentation);
}
