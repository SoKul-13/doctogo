package doctogoserver.service;

import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import doctogoserver.dbrepo.NotificationRepository;
import doctogoserver.dbrepo.UserRepository;
import doctogoserver.model.Notification;
import doctogoserver.model.NotificationStatus;
import doctogoserver.model.NotificationType;
import doctogoserver.model.User;

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository repo;
	
	@Autowired
	private UserRepository userRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	public int create(Integer userId, Double longitude, Double latitude, String notificationType, 
			Double bloodPressure, Integer heartRate, Double oxygenSat) {
		Optional<User> u = userRepo.findById(userId) ;
		if(u.isPresent()) {			
			Notification n = new Notification();
			n.setUser(u.get());
			n.setLongitude(longitude);
			n.setLatitude(latitude);
			n.setBloodPressure(bloodPressure);
			n.setHeartBeat(heartRate);
			n.setOxygenSaturation(oxygenSat);			
			n.setNotificationType(NotificationType.valueOf(notificationType).ordinal());
			n.setCreatedDate(new Date(System.currentTimeMillis()));
			
			Notification result = repo.save(n);
			if(result != null) {
				logger.info(" Notification created for user  "+ userId);
				return 1;
			}else 
				return -1;
		}
		return -1;
	}
	
	/**
	 * Get the Notification for the id
	 * @param nId
	 * @return
	 */
	public Notification get( Integer nId) {
		Optional<Notification> n = repo.findById(nId);
		if(n.isPresent())
			return n.get();
		else 
			return null;		
	}
	
	
	public Iterable<Notification> findAll(){
		return repo.findAll();
	}	
	
	public Set<Notification> findAllByUserId(Integer userId){
		Optional<User> u = userRepo.findById(userId) ;
		if(u.isPresent()) {
			User user = u.get();
			return user.getNotifications();
		}
		return new HashSet<Notification>();
	}	
	
	public Set<Notification> findAllByUserEmail(String email){
		User user = userRepo.findByEmail(email);
		if(user !=null) {
			return user.getNotifications();
		}else {
			this.logger.error(" User does not exists with email :"+ email);
		}
		return null;
	}	
	
	public Iterable<Notification> findAllNewNotifications(){
		return repo.findAllByStatus(NotificationStatus.NEW);
	}
	
	public boolean acceptNotificationForDispatcher(Integer dispatherId, Integer nId){
		Optional<User> u = userRepo.findById(dispatherId) ;
		Optional<Notification> n = repo.findById(nId) ;
		
		if(u.isPresent() && n.isPresent()) {
			//Assign the dispatcher to the notification
			User user = u.get();
			Notification notif = n.get();
			notif.setDispatcher(user);
			notif.setStatus(NotificationStatus.DISPATCHER_ASSIGNED);	
			repo.save(notif);
			return true;
		}
		return false;
	}	
	
	public boolean assignHelperToNotification(Integer helperId , Integer nId){		
		Optional<Notification> n = repo.findById(nId) ;
		Optional<User> u = userRepo.findById(helperId) ;
		if(n.isPresent() && u.isPresent()) {
			Notification notif = n.get();			
			User user = u.get();	
			if(user.isHelper()) {			
				notif.setStatus(NotificationStatus.HELPER_ASSIGNED);	
				notif.getHelpers().add(user);	
				repo.save(notif);		
				user.getAssignedNotifications().add(notif);
				userRepo.save(user);
				return true;
			}else {
				logger.error(" User is not registered as helper for  "+ helperId );
			}			
		}else
			logger.error("Helper or notification does not exists for  "+ helperId );
		
		return false;
	}
	
	public boolean assignHelpersToNotification(Integer[] helperIds , Integer nId){		
		Optional<Notification> n = repo.findById(nId) ;		
		if(n.isPresent()) {
			Notification notif = n.get();
			notif.setStatus(NotificationStatus.HELPER_ASSIGNED);
			for(Integer helperId : helperIds) {		
				Optional<User> u = userRepo.findById(helperId) ;
				if(u.isPresent()) {
					//Assign the helper to the notification
					User user = u.get();	
					notif.getHelpers().add(user);	
					user.getAssignedNotifications().add(notif);
					userRepo.save(user);
				}else {
					logger.error(" Invalid helper id passed "+ helperId );
				}
			}
			repo.save(notif);			
			return true;
		}
		return false;
	}
	public Set<Notification> findAssignedNotificationsForHelper(Integer helperId){
		Optional<User> u = userRepo.findById(helperId) ;		
		if(u.isPresent()) {
			User user = u.get();
			return user.getAssignedNotifications();
		}
		return null;
	}
	
	public boolean acceptNotificationForHelper(Integer helperId, Integer nId){
		Optional<User> u = userRepo.findById(helperId) ;
		Optional<Notification> n = repo.findById(nId) ;
		
		if(u.isPresent() && n.isPresent()) {
			//Mark the notification accepted by helper
			User user = u.get();
			Notification notif = n.get();
			notif.getHelpers().add(user);
			notif.setStatus(NotificationStatus.COMPLETED);		
			repo.save(notif);
			return true;
		}
		return false;
	}
}
