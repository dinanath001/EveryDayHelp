package com.portal.everyday.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.everyday.entity.BookingEmployee;
import com.portal.everyday.entity.Employee;
import com.portal.everyday.entity.FeedBack;
import com.portal.everyday.entity.UserDetails;
import com.portal.everyday.repository.ContactRepository;
import com.portal.everyday.repository.UserRepository;
import com.portal.everyday.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {

 
	
	private final UserService userService; //loose coupling
	public UserController(UserService userService) //cons- dependency injection
	{
		this.userService = userService;
		
	}
	
	@GetMapping("/userAdd")
	public String userAddHandler() 
	{
		return "user/user_Signup"; 
	}
	@PostMapping("/userSignup")
	public String userRegistration(RedirectAttributes rat , UserDetails userDetails, @RequestParam("profilepic") MultipartFile pic)
	{
		userService.addUser(userDetails,pic);
		rat.addFlashAttribute("mssg","Your Registration Done Successfully");
		return "redirect:/user/userAdd";
	}
//	GET /userAdd → shows the registration form (user/user_Signup.html)
//	POST /userSignup → processes the registration and redirects with a success message
	
//user login /user home/	

//user Login form show using Get	
	@GetMapping("/userLoginForm")
	public String userloginShowform()
	{
		return "user/user_login";
	}
	
//user login process by Postmapping
	@PostMapping("/userLogin")
	public String userLogin(@RequestParam String email,@RequestParam("password") String pass, Model model,HttpSession session,RedirectAttributes rat)
	{
	  UserDetails user= userService.userLogin(email,pass);
	  if(user!=null)
	  {
		 
		  //model.addAttribute("userInfo",user);
		  session.setAttribute("userKey", user); //to track the user activity & identify the logged-in-User
		  System.out.println("✅ Logged in user: " + user.getEmail());
		  return "redirect:/user/userHome"; //after verification send user to their Home page(user_home->GetMap)		  
	  }
	  else
	  {
		  rat.addFlashAttribute("mssg","Invalid user Credientials-😢😢😢");
		  return "redirect:/user/userLoginForm";
	  }
	  
	}

//user Home after verification
	@GetMapping("/userHome")
	public String userHomeHandler(HttpSession session,Model model)
	{
		UserDetails user = (UserDetails) session.getAttribute("userKey");
							
		if(user==null)
		{
			
			return "redirect:/user/userLoginForm";
		}
		model.addAttribute("userInfo",user);
		return "user/user_home"; //user_home-->thymeleaf view  		
	}
	
	//or alternate way to get the data of userDetails through the SessionKey
	
//	@GetMapping("/userHome")
//	public String userHomeHandler(@SessionAttribute("userKey") UserDetails user,Model model)
//	{
//		
//		if(user==null)
//		{
//			return "redirect:/user/userLoginForm";
//		}
//		model.addAttribute("userInfo",user);
//		return "user/user_home"; //user_home-->thymeleaf view  		
//	}
	
	
//Logout getMapping 
	@GetMapping("/userLogout")
	public String userLogout(HttpSession session,RedirectAttributes rat)
	{
		    session.removeAttribute("userKey");
		    session.invalidate(); //destroy the session
		    rat.addFlashAttribute("mssg","Successfully Logged out(User)");
			return "redirect:/user/userLoginForm";
			
	}

	
//✅ Solution:
//You must only show the Edit Profile link in the navbar when a user is logged in, not an admin.	
//user edit-profile Get-form
	@GetMapping("/editProfile")
	public String editUserShow(@SessionAttribute("userKey") UserDetails user, Model model) {
	    if (user == null)
	    {
	        return "redirect:/user/userLoginForm";
	    }
	    model.addAttribute("userInfo", user);  // Prefill form with current user data
	    return "user/edit_profile"; //->return html page
	}
	
//user-edit PostMap -Process
	@PostMapping("/updateProfile")
	public String editUserExecute(HttpSession session,@ModelAttribute UserDetails updatedUser,@RequestParam("profileImage") MultipartFile updatedPic)
	{
		System.out.println("Update/Edit Check:"+updatedUser.getEmail());
		
// 1. Update user in DB	
		 UserDetails user= userService.updateUser(updatedUser,updatedPic);
	  
// 2. Replace session object with updated user
	    session.setAttribute("userKey", user);
	    
// 3. Redirect to home (fresh GET with updated session data)
         return "redirect:/user/userHome";
	}
	
	
	@GetMapping("/feedbackForm")
	public String showFeedbackfoorm(@SessionAttribute("userKey") UserDetails user,Model model)
	{
		 model.addAttribute("userInfo",user);
		return "user/feedback";
	}	
	
	@PostMapping("/saveFeedback")
	public String saveFeedback(@ModelAttribute FeedBack feedback, HttpSession session, RedirectAttributes rat) {
	    UserDetails user = (UserDetails) session.getAttribute("userKey");
	    userService.saveFeedback(feedback, user.getEmail());
	    rat.addFlashAttribute("mssg", "Thank you for your feedback!");
	    return "redirect:/user/userHome";
	}
	
//	@PostMapping("/saveFeedback")
//	public String saveFeedback(@ModelAttribute FeedBack feedback, HttpSession session, RedirectAttributes rat) {
//	    UserDetails user = (UserDetails) session.getAttribute("userKey");
//
//	    // bind user here
//	    feedback.setUser(user);  
//
//	    userService.saveFeedback(feedback);
//	    rat.addFlashAttribute("mssg", "Thank you for your feedback!");
//	    return "redirect:/user/userHome";
//	}
	@GetMapping("/Emp_serviceInfo") // Load page initially
	public String loadServicePage(HttpSession session, Model model) {
	    return "user/all_services";
	} 
	 
	 @GetMapping("/Emp_serviceResult")
	 public String getEmpServiceHandler(HttpSession session, @RequestParam String service_type,
			                            @RequestParam String employee_type,Model model)
	 {
		Employee returnEmp = userService.employeeInfo(service_type,employee_type);	
		model.addAttribute("employeeInfo",returnEmp);
		UserDetails user = (UserDetails) session.getAttribute("userKey");
		model.addAttribute("userInfo",user);
		return "user/all_services";
		
	 }
	 //when we use require= false then we need only one @GetMapp --> for showing form nd
	 //get result on the same page ...
	 

//@Get for booking form with email,servicetyp,employeetype//taking data from //->@GetMapping("/Emp_serviceResult")
	  @GetMapping("/bookService")
	  public String showBookingform(@RequestParam String email,
			                        @RequestParam String serviceType,
			                        @RequestParam String employeeType,Model model )
	  {
		  model.addAttribute("userEmail",email);
		  model.addAttribute("serviceType",serviceType);
		  model.addAttribute("employeeType",employeeType);
		  return "user/book_service"; 
	  }		
 /* When user submits the form on /Emp_serviceInfo, it redirects to
 * /Emp_serviceResult. The result page shows matching employee (employeeInfo)
 * and a “Book Now” button. Clicking Book Now should open /bookService with
   email, serviceType, and employeeType prefilled.-->on bookService*/ 
	
     @PostMapping("/bookServiceDo")
     public String saveBookingDetails(@ModelAttribute BookingEmployee bookingEmployee,RedirectAttributes rat)
     {
    	 userService.saveEmpBooking(bookingEmployee);
    	 rat.addFlashAttribute("mssg", "Booking submitted successfully!😎😎");
    	 return "redirect:/user/userHome";
     }
  
//show all confirm booking to User_Dashboard     
     @GetMapping("/bookingStatus")
     public String showallBookingStatus(@SessionAttribute("userKey") UserDetails user, Model model) {
         List<BookingEmployee> bke = userService.bookingStatus(user.getEmail()); // fetch confirmed bookings
         System.out.println("lst......."+bke.get(0));
         model.addAttribute("bookingKey", bke); // pass to Thymeleaf
         return "user/booking_status"; // return view
     }



        
   //1️⃣ Show forgot-password form
     @GetMapping("/forgot-password")
     public String showForgotPasswordForm() {
         return "user/forgot-password";  // ✅ points to templates/user/forgot-password.html
     }

     //2️⃣ Handle forgot-password form submission
     @PostMapping("/forgot-password")
     public String handleForgotPassword(@RequestParam String email, Model model) {
         userService.generateResetToken(email);
         model.addAttribute("message", "If this email exists, a reset link has been sent.");
         return "user/forgot-password";  // ✅ same page with message
     }

     //3️⃣ Show reset-password form (from email link)
     @GetMapping("/reset-password")
     public String showResetForm(@RequestParam String token, Model model) {
         model.addAttribute("token", token);
         return "user/reset-password";   // ✅ changed from "reset-password" to "user/reset-password"
     }

     //4️⃣ Handle reset-password form submission
     @PostMapping("/reset-password")
     public String handleResetPassword(@RequestParam String token,
                                       @RequestParam String password,
                                       Model model) {
         boolean success = userService.resetPassword(token, password);
         model.addAttribute("message", success ? "Password reset successfully!" : "Invalid or expired token.");
         return "user/reset-password";   // ✅ changed from "reset-password" to "user/reset-password"
     }

  


	

	
	
}
