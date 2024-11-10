package es.roomie.household.repository;

import es.roomie.household.model.Household;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseholdRepository extends MongoRepository<Household, String> {

    @Query("{members: {$elemMatch: {email: ?0, invitationAccepted: true}}}")
    List<Household> findByMembersEmail(String memberEmail);

    @Query("{ '_id': ?0, 'members': { '$elemMatch': { 'userId': ?1, 'role': 'admin' } } }")
    Optional<Household> findByIdAndMembersUserIdAndMembersRoleAdmin(String householdId, String userId);

    @Query("{ '_id':  ?0, 'members': { '$elemMatch' :  { 'userId': ?1 } } }")
    Optional<Household> findByIdAndMemberUserId(String householdId, String userId);
}
