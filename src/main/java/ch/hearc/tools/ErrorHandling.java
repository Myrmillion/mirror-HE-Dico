package ch.hearc.tools;

import java.util.Arrays;
import java.util.List;

import org.springframework.ui.ModelMap;

public class ErrorHandling {
	public static List<String> createErrors(String ... errors)
	{
		return Arrays.asList(errors);
	}
	
	public static ModelMap activateErrors(ModelMap model, String ...errors)
	{
		model.addAttribute("error", true);
		model.addAttribute("errors",createErrors(errors));
		return model;
	}
}
