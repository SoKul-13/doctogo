package doctogoserver.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doctogoserver.dbrepo.UserRepository;
import doctogoserver.model.User;

@Service
public class UserService {
	@Autowired
	private UserRepository repo;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public User create(String firstName, String password,String lastName, String email, String phone, 
			String userName, Boolean dispatcherFlag, Boolean helperFlag, 
			String longitude, String lattitude) {
		User n = new User();
		n.setFirstName(firstName);
		n.setLastName(lastName);
		n.setPhone(phone);
		n.setUserName(userName);
		n.setEmail(email);
		n.setDispatcherFlag(dispatcherFlag);
		n.setHelper(helperFlag);
		n.setLongitude(Double.parseDouble(longitude));
		n.setLatitude(Double.parseDouble(lattitude));
		n.setPassword(password);
		User result = repo.save(n);
		return result;
	}
	
	public int update( String firstName, String password, String lastName, String email, String phone, 
			String userName, Boolean dispatcherFlag, Boolean helperFlag) {
		User n = repo.findByEmail(email);
		//Only update existing user
		if(n !=null) {
			n.setFirstName(firstName);
			n.setLastName(lastName);
			n.setPhone(phone);
			n.setUserName(userName);
			n.setEmail(email);
			n.setDispatcherFlag(dispatcherFlag);
			n.setHelper(helperFlag);
			n.setPassword(password);
			
			User result = repo.save(n);
			if(result != null) {
				logger.info(" User updated "+ email);
				return 1;
			}else 
				return -1;
		}else {
			logger.info(" Could not find user with email "+ email + ", no update made");
		}
		return -1;
	}
	
	public void deleteByEmail( String email) {
		User n = repo.findByEmail(email);
		//Only delete if user found.
		if(n !=null) {
			repo.delete(n);			
		}
	}
	
	public void deleteById( Integer id) {
		if(id > 0 && repo.existsById(id)) {
			repo.deleteById(id);
		}
	}
	
	/**
	 * Gets the user for id
	 * @param id
	 * @return
	 */
	public User get( Integer id) {
		Optional<User> u = repo.findById(id) ;
		if(u.isPresent())
			return u.get();
		else return null;
	}
	
	public Iterable<User> findAll(){
		return repo.findAll();
	}
	
	public Iterable<User> findAllHelpers(){
		return repo.findByHelper(true);
	}
	
	public Iterable<User> findAllDispatchers(){
		return repo.findByDispatcherFlag(true);
	}

	public User login(String email, String password) {
		User u = repo.findByEmailAndPassword(email,password);
		logger.info(" Found user "+u);
		
		if(u != null ) {
			u.setPassword(null);
			return u;
		}else 
			return null; 
	}	
}
