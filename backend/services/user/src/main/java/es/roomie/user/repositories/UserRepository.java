package es.roomie.user.repositories;

import es.roomie.user.model.User;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository interface for {@link User} entities.
 * It extends the MongoRepository interface, allowing
 * for CRUD operations and custom query methods.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a list of User objects by their email addresses.
     *
     * @param userEmails a Set of email addresses to search for.
     * @return a List of User objects with matching email addresses,
     *         containing only their IDs.
     */
    @Query(value = "{ 'email':  { '$in':  ?0  } }", fields = "{ _id:  1}")
    List<User> findUserIdsByEmails(Set<String> userEmails);
}
