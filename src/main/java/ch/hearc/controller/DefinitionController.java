package ch.hearc.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import ch.hearc.repository.DefinitionRepository;
import ch.hearc.repository.TagRepository;
import ch.hearc.repository.UserRepository;
import ch.hearc.service.UserService;
import ch.hearc.tools.MessageHandling;
import ch.hearc.wrapper.DefinitionWrapper;
import ch.hearc.wrapper.TagSelected;

@Controller
public class DefinitionController {

	@Autowired
	private DefinitionRepository definitionRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public ModelAndView home(ModelMap model) {

		return new ModelAndView("home");
	}
	@GetMapping("/searchByTag")
	public ModelAndView searchByTag(@RequestParam(value = "name", required = true) String name, ModelMap model) {
		if (name.length() > 0) {
			Tag tag = tagRepository.findByName(name);
			if(tag!=null)
			{
				Set<Definition> definitions = tag.getContainedTags();
				if(definitions.size()>0)
				{
					model.addAttribute("definitions", definitions);

					// upvotes and downvotes
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					User user = userService.findByUsername(auth.getName());
					if (user != null) {
						List<Integer> upvotes = user.getUpvotedDefinitions().stream().map(Definition::getId)
								.collect(Collectors.toList());
						List<Integer> downvotes = user.getDownvotedDefinitions().stream().map(Definition::getId)
								.collect(Collectors.toList());
						model.addAttribute("upvotes", upvotes);
						model.addAttribute("downvotes", downvotes);
					}
				}
				else {
					return MessageHandling.redirectWithErrors("redirect:/", "No definition found for this tag !");
				}
			}
			else
			{
				return MessageHandling.redirectWithErrors("redirect:/", "No tag found with this name !");
			}
					
		}
		return new ModelAndView("home",model);
	}

	@GetMapping("/search")
	public ModelAndView search(@RequestParam(value = "word", required = true) String word, ModelMap model) {
		if (word.length() > 0) {
			List<Definition> definitions = definitionRepository.findByWordOrderByNupvoteDesc(word);

			if (definitions.size() > 0) {
				model.addAttribute("definitions", definitions);

				// upvotes and downvotes
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				User user = userService.findByUsername(auth.getName());
				if (user != null) {
					List<Integer> upvotes = user.getUpvotedDefinitions().stream().map(Definition::getId)
							.collect(Collectors.toList());
					List<Integer> downvotes = user.getDownvotedDefinitions().stream().map(Definition::getId)
							.collect(Collectors.toList());
					model.addAttribute("upvotes", upvotes);
					model.addAttribute("downvotes", downvotes);
				}
			} else {
				return MessageHandling.redirectWithErrors("redirect:/", "No definition found for this word !");
			}
		}
		return new ModelAndView("home", model);
	}

	@GetMapping("/definitions")
	public ModelAndView myDefinitions(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User creator = userService.findByUsername(auth.getName());
		if (creator != null) {
			List<Definition> definitions = creator.getDefinitions();

			if (definitions.size() > 0) {
				model.addAttribute("definitions", definitions);
			}
			
			return new ModelAndView("my-definitions", model);
		} else {
			return new ModelAndView("home", model);
		}
	}

	@RequestMapping(value = "/definition/edit/{id}", method = RequestMethod.GET)
	public ModelAndView formEdit(@PathVariable int id, ModelMap model) {
		Definition definition = definitionRepository.findById(id);

		if (definition != null) {
			List<Tag> tags = tagRepository.findAll();
			DefinitionWrapper definitionWrapper = new DefinitionWrapper();
			definitionWrapper.setDefinition(definition);
			List<Integer> tagsDefinition = definition.getContainingTags()//
					.stream()//
					.map(Tag::getId)//
					.collect(Collectors.toList());
			List<TagSelected> tagsSelected = new LinkedList<TagSelected>();
			for (Tag tag : tags) {
				TagSelected tagSelected = new TagSelected();
				tagSelected.setTag(tag);
				boolean selected = false;
				if (tagsDefinition.contains(tag.getId())) {
					selected = true;
				}
				tagSelected.setSelected(selected);
				tagsSelected.add(tagSelected);
			}
			definitionWrapper.setTags(tagsSelected);
			model.addAttribute("definitionWrapper", definitionWrapper);
			return new ModelAndView("formEditDef", model);
		} else {
			return new ModelAndView("my-definitions", model);
		}

	}

	@PostMapping("/EditDefinition")
	public ModelAndView editDefinition(@Validated @ModelAttribute DefinitionWrapper definitionWrapper,
			BindingResult errors, ModelMap model, RedirectAttributes redirAttrs, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		if (!errors.hasErrors())
		{
			Definition definitionFromWrapper = definitionWrapper.getDefinition();
			Definition definitionToUpdate = definitionRepository.findById(definitionFromWrapper.getId());
			
			if (definitionToUpdate != null)
			{
				definitionFromWrapper.setCreator(definitionToUpdate.getCreator());
				
				if (definitionFromWrapper.validate())
				{
					definitionToUpdate.setWord(definitionFromWrapper.getWord());
					definitionToUpdate.setDescription(definitionFromWrapper.getDescription());
					Set<Tag> tags = definitionWrapper.getTags().stream()//
							.filter(TagSelected::getSelected)//
							.map(TagSelected::getTag)//
							.collect(Collectors.toSet());//

					definitionToUpdate.setContainingTags(tags);
					definitionRepository.save(definitionToUpdate);
					
					return MessageHandling.redirectWithSuccess("redirect:/definitions", "Definition Updated !");
				}
				return MessageHandling.redirectWithErrors("redirect:" + referer, "Missing fields !");
			}

		}

		return MessageHandling.redirectWithErrors("redirect:" + referer, "Couldn't updated definition.");
	}

	@RequestMapping(value = "/definition/delete", method = RequestMethod.POST)
	public ModelAndView deleteDefinition(@RequestParam("definitionID") int definitionID, ModelMap model,
			RedirectAttributes redirAttrs) {
		Definition definitionToDelete = definitionRepository.findById(definitionID);
		if (definitionToDelete != null) {
			definitionRepository.delete(definitionToDelete);
		}

		return MessageHandling.redirectWithSuccess("redirect:/definitions", "Definition deleted !");
	}

	@PostMapping("/Upvote")
	public ModelAndView upvote(@RequestParam("definitionID") int definitionID, ModelMap model,
			RedirectAttributes redirAttrs, HttpServletRequest request) {
		Definition definition = definitionRepository.findById(definitionID);
		if (definition != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByUsername(auth.getName());
			if (user != null) {
				boolean upvoted = user.getUpvotedDefinitions()//
						.stream()//
						.map(Definition::getId)//
						.anyMatch(idUpvoted -> idUpvoted.intValue() == definition.getId().intValue());
				if (upvoted) {
					MessageHandling.activateErrors(model, "Definition already upvoted.");
				} else {
					definition.setNupvote(definition.getNupvote() + 1);
					user.getUpvotedDefinitions().add(definition);
					userRepository.save(user);
					definitionRepository.save(definition);
				}
			}

		} else {
			MessageHandling.activateErrors(model, "Cannot delete this definition.");
		}
		String referer = request.getHeader("Referer");
		return new ModelAndView("redirect:" + referer, model);
	}

	@PostMapping("/Downvote")
	public ModelAndView downvote(@RequestParam("definitionID") int definitionID, ModelMap model,
			RedirectAttributes redirAttrs, HttpServletRequest request) {
		Definition definition = definitionRepository.findById(definitionID);
		if (definition != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByUsername(auth.getName());

			if (user != null) {
				boolean downvoted = user.getDownvotedDefinitions()//
						.stream()//
						.map(Definition::getId)//
						.anyMatch(idDownvoted -> idDownvoted.intValue() == definition.getId().intValue());
				if (downvoted) {
					MessageHandling.activateErrors(model, "Definition already downvoted.");
				} else {
					definition.setNdownvote(definition.getNdownvote() + 1);
					user.getDownvotedDefinitions().add(definition);
					userRepository.save(user);
					definitionRepository.save(definition);
				}
			}

		} else {
			MessageHandling.activateErrors(model, "Cannot delete this definition.");
		}
		String referer = request.getHeader("Referer");
		return new ModelAndView("redirect:" + referer, model);

	}

	@PostMapping("/UnDownvote")
	public ModelAndView unDownvote(@RequestParam("definitionID") int definitionID, ModelMap model,
			RedirectAttributes redirAttrs, HttpServletRequest request) {
		Definition definition = definitionRepository.findById(definitionID);

		if (definition != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByUsername(auth.getName());

			if (user != null) {
				user.getDownvotedDefinitions().removeIf(def -> def.getId().intValue() == definitionID);
				definition.setNdownvote(definition.getNdownvote() - 1);
				userRepository.save(user);
				definitionRepository.save(definition);
			} else {
				MessageHandling.activateErrors(model, "Not connected");
			}
		} else {
			MessageHandling.activateErrors(model, "Cannot take out the downvote.");
		}

		String referer = request.getHeader("Referer");
		return new ModelAndView("redirect:" + referer, model);
	}

	@PostMapping("/UnUpvote")
	public ModelAndView unUpvote(@RequestParam("definitionID") int definitionID, ModelMap model,
			RedirectAttributes redirAttrs, HttpServletRequest request) {
		Definition definition = definitionRepository.findById(definitionID);

		if (definition != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByUsername(auth.getName());

			if (user != null) {
				user.getUpvotedDefinitions().removeIf(def -> def.getId().intValue() == definitionID);
				definition.setNupvote(definition.getNupvote() - 1);
				userRepository.save(user);
				definitionRepository.save(definition);
			} else {
				MessageHandling.activateErrors(model, "Not connected");
			}
		} else {
			MessageHandling.activateErrors(model, "Cannot take out the upvote.");
		}

		String referer = request.getHeader("Referer");
		return new ModelAndView("redirect:" + referer, model);
	}

	/**
	 * Function called when a user wants to add a new definition
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/definition/create")
	public String form(ModelMap model) {

		DefinitionWrapper definitionWrapper = new DefinitionWrapper();
		List<Tag> tags = tagRepository.findAll();

		List<TagSelected> tagsSelected = tags.stream().map(tag -> {
			TagSelected tagSelected = new TagSelected();
			tagSelected.setTag(tag);
			return tagSelected;
		}).collect(Collectors.toList());

		definitionWrapper.setTags(tagsSelected);

		definitionWrapper.setDefinition(new Definition());
		model.put("definitionWrapper", definitionWrapper);

		//////////////
		// UPDATE //
		// CHAP_06 //
		//////////////

		// Retrieve the current context authentication
		/*
		 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 * User creator = userService.findByUsername(auth.getName());
		 */
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
	public ModelAndView save(@Validated @ModelAttribute DefinitionWrapper definitionWrapper, BindingResult errors,
			ModelMap model, RedirectAttributes redirAttrs) {

		System.out.println(errors);
		if (!errors.hasErrors()) {
			//////////////
			// UPDATE //
			// CHAP_06 //
			//////////////
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User creator = userService.findByUsername(auth.getName());
			Definition definition = definitionWrapper.getDefinition();
			if (creator != null && definition != null) {
				definition.setCreator(creator);

				// Save using Chap5 JPA query
				if (definition.validate()) {
					if (definitionWrapper.getTags() != null) {
						Set<Tag> tags = definitionWrapper.getTags().stream()//
								.filter(TagSelected::getSelected)//
								.map(TagSelected::getTag)//
								.collect(Collectors.toSet());//

						definition.setContainingTags(tags);
					}
					definitionRepository.save(definition);
				} else {
					return MessageHandling.redirectWithErrors("redirect:/definition/create",
							"Please fill all the fields !");
				}
			}
		} else {
			return MessageHandling.redirectWithErrors("redirect:/definition/create", "Errors in fields !");
		}
		return MessageHandling.redirectWithSuccess("redirect:/definitions", "Definition created !");
		// return ((errors.hasErrors()) ? "home" : "redirect:/definitions");
	}

}
