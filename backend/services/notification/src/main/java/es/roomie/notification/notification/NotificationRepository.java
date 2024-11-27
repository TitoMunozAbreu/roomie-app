package es.roomie.notification.notification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing {@link Notification} entities in the database.
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
}
