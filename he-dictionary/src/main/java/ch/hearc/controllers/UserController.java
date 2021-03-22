package ch.hearc.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ch.hearc.models.User;
import ch.hearc.service.UserService;

//////////////
//  UPDATE  //
//  CHAP_6 //
//////////////

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Select the login view to render by returning its name
	 */
	@GetMapping("/login")
	public String login(Map<String, Object> model) {		
		return "login";
	}
	
	@PostMapping("/login")
	public String connectUser(Map<String, Object> model) {	
		
		return "login";
	}

	@GetMapping("/signup")
	public String signup(Map<String, Object> model) {
		User user = new User();
		model.put("user", user);
		
		return "signup";
	}

	@PostMapping("/signup")
	public String createAuthor(@Valid User author, BindingResult bindingResult, Map<String, Object> model) {

		User userExists = userService.findByUsername(author.getUsername());

		if (userExists != null) {
			model.put("msg", "User already exists.");
			return ("redirect:/signup?error=true");
		}
		else if(author.getPassword() != author.getConfirmPassword())
		{
			model.put("msg", "Passwords does not match");
			return ("redirect:/signup?error=true");
		}
		else if (bindingResult.hasErrors()) 
		{
			model.put("msg", "Error in fields.");
			return ("redirect:/signup?error=true");
		}
		else 
		{
			userService.saveUser(author);
			model.put("msg", "User has been registered successfully!");
			//model.put("author", new User());
			model.put("author", author);
			
			return "login";
		}
	}

}