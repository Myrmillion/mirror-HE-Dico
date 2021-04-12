package ch.hearc.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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

	@GetMapping("/signup")
	public String signup(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		
		
		return "signup";
	}

	@PostMapping("/signup")
	public ModelAndView createAuthor(@Valid User author, BindingResult bindingResult, ModelMap model) {

		User userExists = userService.findByUsername(author.getUsername());
		List<String> errors = new LinkedList<String>();
		if (userExists != null) {
			//model.addAttribute("msg", "User already exists.");
			errors.add("User already exists.");
			//return ("redirect:/signup?error=true");
		}
		if(!author.getPassword().equals(author.getConfirmPassword()))
		{
			//model.addAttribute("msg", "Passwords does not match");
			errors.add("Passwords does not match");
			// ("redirect:/signup?error=true");
		}
		if (bindingResult.hasErrors()) 
		{
			//model.addAttribute("msg", "Error in fields.");
			errors.add("Error in fields.");
			//return ("redirect:/signup?error=true");
		}
		if(!errors.isEmpty())
		{
			model.addAttribute("errors", errors);
			return new ModelAndView("redirect:/signup?error=true", model);
		}
		else 
		{
			userService.saveUser(author);
			//model.put("msg", "User has been registered successfully!");
			//model.put("author", new User());
			model.addAttribute("author", author);
			
			return new ModelAndView("login");
		}
	}

}