package com.portal.everyday.controller;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.everyday.entity.Contact;
import com.portal.everyday.service.PublicService;

@Controller //send's html view 
public class PublicController {

	@Value("${app.phone}")
	String phone;
	
	private final PublicService publicService;
	
	public PublicController(PublicService publicService)
	{
		this.publicService = publicService;
	}
	
	
//	@ModelAttribute
//	public void setTitle(Model model)
//	{
//		model.addAttribute("title","EveryDayHelp"); -> for Specific Controller
//	}
	
	
	

	@GetMapping({"/","home"}) //we can assign more than one end points for one service
	public String home(Model model)
	{
		//controller->view = Model
		//view->Controller = ModelAttribute
		 //To create Global data access Method= ModelAttribute
		 model.addAttribute("mssg","Easy Day Help portal");
		 return "public/home"; //it is a view name /-> html page name  but without --> .html
	  
	}
	
	@GetMapping("/aboutUs")
	public String aboutUs(Model model)
	{
		model.addAttribute("GlobalphoneKey",phone);
		return "public/about_us";
	}
	
	@GetMapping("/contactUs")
	public String contact_Us()
	{
		return "public/contact_us";
	}
	
//	@PostMapping("/contactUs")
//	public String addContact(Model model, Contact contact) //all name controlles should be same in entity=html
//	{
//		publicService.addContact(contact);
//		model.addAttribute("mssg","Thnkyou for Contacting Us");
//   	return "public/contact_us";// carry old request's
//		
//		return "redirect:/contactUs"; //in redirect browser always generates  a new request
//		//get mapping url is used in redirect
//	}
	@PostMapping("/contactUs")
	public String addContact(RedirectAttributes rat, Contact contact) //all name controlles should be same in entity=html
	{
		publicService.addContact(contact);
		rat.addFlashAttribute("mssg","Thnkyou for Contacting Us");	
		return "redirect:/contactUs"; //in redirect browser always generates  a new request
		//get mapping url is used in redirect
	}
	
	

}
