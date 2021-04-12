package ch.hearc.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.hearc.models.Definition;
import ch.hearc.repository.DefinitionRepository;
import ch.hearc.service.UserService;

@Controller
public class DefinitionController {

	@Autowired
	private DefinitionRepository definitionRepository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/search")
	public ModelAndView search(@RequestParam(value = "word", required = true) String word, ModelMap model) {
		/*
		if(word.length()>0)
		{
			List<Definition> definitions = definitionRepository.findByWordUpvoteDsc(word);
			
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
		*/
		return new ModelAndView("/",model);
	}
}
