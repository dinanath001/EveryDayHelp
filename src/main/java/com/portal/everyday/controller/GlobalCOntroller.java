package com.portal.everyday.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalCOntroller {
	
	@Value("${app.title}")
	String webSitetitle;
	
	@ModelAttribute
	public void setTitle(Model model)
	{
		model.addAttribute("title",webSitetitle);
	}
	
	//we can provide anything for our Template Page like  in Header,footer or any common message

}
