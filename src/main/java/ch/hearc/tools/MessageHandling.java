package ch.hearc.tools;

import java.util.Arrays;
import java.util.List;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

public class MessageHandling {
	public static List<String> createMessages(String ... messages)
	{
		return Arrays.asList(messages);
	}
	
	public static ModelMap activateErrors(ModelMap model, String ...errors)
	{
		model.addAttribute("error", true);
		model.addAttribute("errors",createMessages(errors));
		return model;
	}
	
	public static ModelAndView redirectWithErrors(String redirect, String ...errors)
	{
		ModelAndView model = new ModelAndView(redirect);
		model.addObject("error",true);
		model.addObject("errors", createMessages(errors));
		return model;
	}
	
	public static ModelAndView redirectWithSuccess(String redirect, String ...successes)
	{
		ModelAndView model = new ModelAndView(redirect);
		model.addObject("success",true);
		model.addObject("successes", createMessages(successes));
		return model;
	}
}
