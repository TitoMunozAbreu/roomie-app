package es.roomie.household.repository;

import es.roomie.household.model.Household;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing Household data in MongoDB.
 * This interface extends MongoRepository to provide CRUD operations
 * and custom query methods for the Household entity.
 */
@Repository
public interface HouseholdRepository extends MongoRepository<Household, String> {

    /**
     * Finds all households that have members with the specified email
     * and where the invitation has been accepted.
     *
     * @param memberEmail the email of the member to search for
     * @return a list of households matching the criteria
     */
    @Query("{members: {$elemMatch: {email: ?0, invitationAccepted: true}}}")
    List<Household> findByMembersEmail(String memberEmail);

    /**
     * Finds a household by its ID and checks if a member with the
     * specified userId has the role of 'admin'.
     *
     * @param householdId the ID of the household to search for
     * @param userId the user ID of the member to check for admin role
     * @return an Optional containing the Household if found, otherwise empty
     */
    @Query("{ '_id': ?0, 'members': { '$elemMatch': { 'userId': ?1, 'role': 'admin' } } }")
    Optional<Household> findByIdAndMembersUserIdAndMembersRoleAdmin(String householdId, String userId);

    /**
     * Finds a household by its ID and checks if a member with the
     * specified userId exists.
     *
     * @param householdId the ID of the household to search for
     * @param userId the user ID of the member to look for
     * @return an Optional containing the Household if found, otherwise empty
     */
    @Query("{ '_id':  ?0, 'members': { '$elemMatch' :  { 'userId': ?1 } } }")
    Optional<Household> findByIdAndMemberUserId(String householdId, String userId);

    /**
     * Finds a household by its ID and checks if a member with the
     * specified email exists.
     *
     * @param householdId the ID of the household to search for
     * @param memberEmail the email of the member to look for
     * @return an Optional containing the Household if found, otherwise empty
     */
    @Query("{'_id': ?0, 'members':  { '$elemMatch':  { 'email':  ?1} } }")
    Optional<Household> findByIdAndMemberEmail(String householdId, String memberEmail);
}
