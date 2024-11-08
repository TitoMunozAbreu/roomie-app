package es.roomie.user.repositories;

import es.roomie.user.model.User;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{ 'email':  { '$in':  ?0  } }", fields = "{ _id:  1}")
    List<User> findUserIdsByEmails(Set<String> userEmails);
}
