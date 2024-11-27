package es.roomie.household.service.client.feign;

import es.roomie.household.model.feign.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Feign client interface for interacting with the user service.
 * It allows for fetching user information based on email addresses.
 */
@FeignClient(
        name = "user-service",
        url = "${user.url}")
public interface UserClient {

    /**
     * Retrieves a list of users based on the provided email addresses.
     *
     * @param userEmails a Set of user email addresses for which user details are to be fetched.
     *                   This parameter is required.
     * @return a ResponseEntity containing a List of UserResponse objects, which represent
     *         the details of the users associated with the provided email addresses.
     */
    @GetMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserResponse>> getUsers(@RequestParam(required = true) Set<String> userEmails);
}