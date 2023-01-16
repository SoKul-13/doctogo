package doctogoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import doctogoserver.model.User;
import doctogoserver.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService service;
	
	@PostMapping(path="/create") // Map ONLY POST Requests
	public @ResponseBody User addNewUser (@RequestParam String firstName,@RequestParam String password
			, @RequestParam String lastName,  @RequestParam String email, @RequestParam String phone,  
			@RequestParam String userName, @RequestParam Boolean dispatcherFlag, 
			@RequestParam Boolean helperFlag ,@RequestParam String longitude,@RequestParam String latitude) {
		logger.info(" user for email "+ email);
		return service.create(firstName,password, lastName,email,phone,userName,dispatcherFlag,helperFlag, longitude,latitude);
	}
	
	@PutMapping(path="/update") // 
	public @ResponseBody String update (@RequestParam String firstName,@RequestParam String password
			, @RequestParam String lastName,  @RequestParam String email, @RequestParam String phone,  
			@RequestParam String userName, @RequestParam Boolean dispatcherFlag, @RequestParam Boolean helperFlag  ) {
		int result = service.update(firstName,password, lastName,email,phone,userName,dispatcherFlag,helperFlag);
		logger.info(" user updatation result "+ result + " for email "+ email);
		if(result != -1)
			return "Successfully updated user "+ email;
		else 
			return "Error updating user" ;
	}
	
	@DeleteMapping(path="/delete") 
	public String delete(@RequestParam Integer userId) {
		service.deleteById(userId);
        return " User deleted ";
    }
	
	@DeleteMapping(path="/deleteByEmail") 
	public String deleteByEmail(@RequestParam String email) {
		service.deleteByEmail(email);
        return " User deleted ";
    }
	
	@GetMapping("/")
    public String welcome() {
        return " User service home page";
    }
	
	@GetMapping(path="/findAll")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return service.findAll();
	}
	
	@PostMapping(path="/login")
	public @ResponseBody User login(@RequestParam String email, @RequestParam String password) {
		logger.info(" User "+ email + " logging in");
		return service.login(email,password);
	}
}