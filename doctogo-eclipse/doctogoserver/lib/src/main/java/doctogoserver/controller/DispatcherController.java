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
@RequestMapping("/dispatcher")
public class DispatcherController {

	@Autowired
	private NotificationService service ; 
	@Autowired
	private UserService userService ; 
	
	@GetMapping(path="/getNotifications")
	public @ResponseBody Iterable<Notification> findAllNotifications() {
		return service.findAllNewNotifications();
	}
	
	@PostMapping(path="/acceptNotification") //helpers in the 50 mile radius 
	public @ResponseBody boolean acceptNotificaiton (@RequestParam Integer dispatcherId, 
			@RequestParam Integer notificationId) {
		return service.acceptNotificationForDispatcher(dispatcherId, notificationId);
	}
	@PostMapping(path="/acceptAndFindHelpers") //helpers in the 50 mile radius 
	public @ResponseBody Iterable<User> acceptNotificationAndReturnHelpers (@RequestParam Integer dispatcherId, 
			@RequestParam Integer notificationId) {
		boolean accepted = service.acceptNotificationForDispatcher(dispatcherId, notificationId);
		if(accepted) {
			return findHelpers(notificationId);
		}else {
			return null;
		}
	}
	//Assign the notification to helpers 
	@PostMapping(path="/assignHelper") 
	public @ResponseBody boolean assignHelperToNotification (@RequestParam Integer notificationId, 
			@RequestParam Integer helperId ) {
		return service.assignHelperToNotification(helperId, notificationId);
	}
	
	//Assign the notification to helpers 
	@PostMapping(path="/assignHelpers") 
	public @ResponseBody boolean assignHelpersToNotification (@RequestParam Integer notificationId, 
			@RequestParam Integer[] helperIds ) {
		return service.assignHelpersToNotification(helperIds, notificationId);
	}
	
	@GetMapping(path="/findHelpers") //helpers in the 50 mile radius 
	public @ResponseBody Iterable<User> findHelpers (@RequestParam Integer notificationId) {
		//TODO add 50 mile radius check 
		return userService.findAllHelpers();
	}
}