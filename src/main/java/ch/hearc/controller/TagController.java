package ch.hearc.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.hearc.model.Definition;
import ch.hearc.model.Tag;
import ch.hearc.model.User;
import ch.hearc.repository.TagRepository;
import ch.hearc.repository.UserRepository;
import ch.hearc.service.UserService;
import ch.hearc.tools.MessageHandling;

@Controller
public class TagController {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/tag/create")
	public String formCreate(ModelMap model) {
		model.put("tag", new Tag());

		return "formulaireTag";
	}
	
	@RequestMapping(value = "/tag/edit/{id}", method = RequestMethod.GET)
	public ModelAndView formEdit(@PathVariable int id, ModelMap model) {
		Tag tag = tagRepository.findById(id);
		if (tag != null) {
			model.addAttribute("tag", tag);
			return new ModelAndView("formEditTag", model);
		} else {
			return new ModelAndView("my-tags", model);
		}

	}
	
	@RequestMapping(value = "/tag/delete", method = RequestMethod.POST)
	public ModelAndView deleteDefinition(@RequestParam("tagID") int tagID, ModelMap model,
			RedirectAttributes redirAttrs) {
		Tag tagToDelete = tagRepository.findById(tagID);
		if (tagToDelete != null) {
			tagRepository.delete(tagToDelete);
		}

		return new ModelAndView("my-tags", model);
	}
	
	@PostMapping("/EditTag")
	public ModelAndView editDefinition(@Validated @ModelAttribute Tag tag, BindingResult errors,
			ModelMap model, RedirectAttributes redirAttrs) {
		if (!errors.hasErrors()) {
			Tag tagToUpdate = tagRepository.findById(tag.getId()).get();
			if (tagToUpdate !=null) {
				tagToUpdate.setName(tag.getName());
				tagToUpdate.setColor(tag.getColor());
				tagRepository.save(tagToUpdate);
			}
			return new ModelAndView("my-tags", model);

		} else {
			MessageHandling.activateErrors(model,"Couldn't update tag !");
			return new ModelAndView("my-tags", model);
		}
	}
	
	@GetMapping("/tags")
	public ModelAndView myTags(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User creator = userService.findByUsername(auth.getName());
		if (creator != null) {
			List<Tag> tags = creator.getTags();
			if (tags.size() > 0) {
				model.addAttribute("tags", tags);
			} else {
				MessageHandling.activateErrors(model,"No tags created !");
			}
			return new ModelAndView("my-tags", model);
		} else {
			return new ModelAndView("home", model);
		}
	}
	
	@PostMapping("/SaveTag")
	public String save(@Validated @ModelAttribute Tag tag, BindingResult errors, Model model,
			RedirectAttributes redirAttrs) {

		if (!errors.hasErrors()) {
			//////////////
			// UPDATE //
			// CHAP_06 //
			//////////////
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User creator = userService.findByUsername(auth.getName());
			if (creator != null)
				tag.setCreator(creator);

			// Save using Chap5 JPA query
			if (tag.validate()) {
				System.out.println("YOOOOOOOOOOOOOOOO");
				System.out.println(tag.getColor());
				tagRepository.save(tag);
			}
			else
			{
				throw new RuntimeException("The task is not complete ! Please fill all the fields");
			}
		}
		return ((errors.hasErrors()) ? "tag" : "redirect:/tags");
	}
}
