package doctogoserver.dbrepo;

import org.springframework.data.repository.CrudRepository;

import doctogoserver.model.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByEmail(String email);
	User findByEmailAndPassword(String email, String password);
	Iterable<User> findByHelper(boolean flag);
	Iterable<User> findByDispatcherFlag(boolean flag);
}