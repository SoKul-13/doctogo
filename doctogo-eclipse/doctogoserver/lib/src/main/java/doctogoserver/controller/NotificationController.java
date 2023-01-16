package doctogoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import doctogoserver.model.Notification;
import doctogoserver.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

	@Autowired
	private NotificationService service ; 
	
	@PostMapping(path="/create") // Map ONLY POST Requests
	public @ResponseBody String create (@RequestParam Integer userId
			, @RequestParam Double longitude,  @RequestParam Double latitude, 
			@RequestParam String notificationType, Double bloodPressure, Integer heartRate, Double oxygenSat) {
		service.create(userId,longitude,latitude,notificationType,bloodPressure,heartRate,oxygenSat);
		return "Saved";
	}	
	@GetMapping(path="/myNotifications")
	public @ResponseBody Iterable<Notification> findAllByUser(@RequestParam String email) {
		return service.findAllByUserEmail(email);
	}
	@GetMapping(path="/get")
	public @ResponseBody Notification get(@RequestParam Integer id) {
		return service.get(id);
	}
}