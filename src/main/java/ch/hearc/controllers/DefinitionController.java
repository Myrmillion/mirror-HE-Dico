package ch.hearc.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.text.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.hearc.models.Definition;
import ch.hearc.models.User;
import ch.hearc.repository.DefinitionRepository;
import ch.hearc.service.UserService;

@Controller
public class DefinitionController {

	@Autowired
	private DefinitionRepository definitionRepository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public ModelAndView home(ModelMap model) {
		
		return new ModelAndView("home");
	}
	
	@GetMapping("/search")
	public ModelAndView search(@RequestParam(value = "word", required = true) String word, ModelMap model) {
		if(word.length()>0)
		{
			List<Definition> definitions = definitionRepository.findByWordOrderByNupvoteDesc(word);
			
			if(definitions.size()>0)
			{
				model.addAttribute("definitions",definitions);
			}
			else
			{
				model.addAttribute("error",true);
				model.addAttribute("msg","No definition found with this word.");
			}
		}
		return new ModelAndView("/",model);
	}
	
	/**
	 * Function called when a user wants to add a new todo
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/formDefinition")
	public String form(ModelMap model) 
	{		
		model.put("definition", new Definition());
				
		//////////////
		//  UPDATE  //
		//  CHAP_06 //
		//////////////		
				
		// Retrieve the current context authentication
		/*
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User creator = userService.findByUsername(auth.getName());*/				
		return "formulaireDefinition";
	}

	
	/**
	 * Save a new definition
	 * 
	 * @param definition
	 * @param errors
	 * @param model
	 * @return
	 */
	@PostMapping("/SaveDefinition")
	public String save(@Validated @ModelAttribute Definition definition, BindingResult errors, Model model, RedirectAttributes redirAttrs) {
		
		if (!errors.hasErrors()) 
		{
			//////////////
			//  UPDATE  //
			//  CHAP_06 //
			//////////////			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User creator = userService.findByUsername(auth.getName());
			if (creator != null) 
				definition.setCreator(creator);
						
			
			//Save using Chap5 JPA query
			if(definition.validate())
				definitionRepository.save(definition);
			else
				throw new RuntimeException("The task is not complete ! Please fill all the fields");			
		}
		return ((errors.hasErrors()) ? "formulaireDefinition" : "redirect:/");
	}
	
}
