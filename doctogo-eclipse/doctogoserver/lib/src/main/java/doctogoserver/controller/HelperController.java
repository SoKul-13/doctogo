package doctogoserver.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import doctogoserver.dbrepo.NotificationRepository;
import doctogoserver.dbrepo.UserRepository;
import doctogoserver.model.Notification;
import doctogoserver.model.User;
import doctogoserver.service.NotificationService;
import doctogoserver.service.UserService;

@RestController
@RequestMapping("/helper")
public class HelperController {

	@Autowired
	private NotificationService service ; 
	@Autowired
	private UserService userService ; 
	
	@GetMapping(path="/getAssignedNotifications")
	public @ResponseBody Iterable<Notification> getAssignedNotifications(@RequestParam Integer helperId) {
		return service.findAssignedNotificationsForHelper(helperId);
	}
	
	@PostMapping(path="/acceptNotification")
	public @ResponseBody boolean acceptNotification (@RequestParam Integer helperId, 
			@RequestParam Integer notificationId) {
		return service.acceptNotificationForHelper(helperId, notificationId);
	}
	
	//TODO -remove this method later
	@GetMapping(path="/findAll")
	public @ResponseBody Iterable<User> findAllHelpers() {		
		return userService.findAllHelpers();
	}
}