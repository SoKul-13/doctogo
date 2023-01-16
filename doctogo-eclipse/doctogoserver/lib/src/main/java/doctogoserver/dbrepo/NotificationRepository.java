package doctogoserver.dbrepo;

import org.springframework.data.repository.CrudRepository;

import doctogoserver.model.Notification;
import doctogoserver.model.NotificationStatus;
import doctogoserver.model.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
	Iterable<Notification> findAllByStatus(NotificationStatus status);
}