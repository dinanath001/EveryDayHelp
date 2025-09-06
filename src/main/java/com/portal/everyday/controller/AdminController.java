package com.portal.everyday.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.everyday.entity.Admin;
import com.portal.everyday.entity.AdminNotice;
import com.portal.everyday.entity.BookingEmployee;
import com.portal.everyday.entity.Contact;
import com.portal.everyday.entity.Employee;
import com.portal.everyday.entity.UserDetails;
import com.portal.everyday.service.AdminService;

import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin")
//@RequiredArgsConstructor //parameterized Constructor
public class AdminController {
//	@NonNull
//	private String name;
	private final AdminService adminService; //loose coupling
	public AdminController(AdminService adminService) //constructor based dependency injection
	{
		this.adminService = adminService;
	}
	
	
	
	
//Admin login form 	
	@GetMapping("/adminLogin")
	public String adminLoginHandler()
	{	
		return "admin/admin_login"; //return html view page by GetMapping to show the html form	
	}
//Admin login-process	
	@PostMapping("/adminLogin")
	public String adminLogin(@RequestParam String email,@RequestParam("password") String pass,Model model ,HttpSession session ,RedirectAttributes rat)
	{
	  Admin admin =	adminService.adminLogin(email,pass); //returning Object of that entity
	   if(admin!= null)
	   {
		   //System.out.println(admin.getEmail());
		   session.setAttribute("adminKey", admin);//to track the identity of Admin
		   return "redirect:/admin/adminHome";  // ✅ redirect to avoid form resubmission //after verification send to admin_home(html /view page) 	   
	   }
	   else
	   {
		   rat.addFlashAttribute("mssg","Invalid Credientials");
		   return "redirect:/admin/adminLogin"; //get mapping /end point
	   }
	
	}
	
	/*
	 * Post/Redirect/Get (PRG) pattern POST: User submits login form.
	 * 
	 * REDIRECT: Server verifies login and redirects to /admin/adminHome.
	 * 
	 * GET: The browser then makes a new GET request to /admin/adminHome.
	 */
	
//admin home 
		@GetMapping("/adminHome")
		public String adminHomeHandler(HttpSession session, Model model,RedirectAttributes rat)
		{
			Admin admin = (Admin)session.getAttribute("adminKey");
			
			System.out.println(admin);
			if(admin==null){
				rat.addFlashAttribute("mssg","Un-authorised access,pls do login😢");
				return "redirect:/admin/adminLogin";
			}
			else {
			model.addAttribute("adminInfo",admin); //bind all data of admin(object of Admin)
			return "admin/admin_home";
			}
		}
//logout admin
		@GetMapping("/adminLogout")
		public String adminlogoutHandler(HttpSession session,RedirectAttributes rat)
		{
		    session.removeAttribute("adminKey");
		    session.invalidate(); //destroy the session
		    rat.addFlashAttribute("mssg","Successfully Logged out");
			return "redirect:/admin/adminLogin";
		}		
	
		
		
		
		
	
//to show the Registration Form for Admin using -->GET	
	@GetMapping("/addEmployee")
	public String showEmployeeform(HttpSession session)
	{
		return "admin/register_employee"; //admin->folder -->register_employee(html page)file
	}
//to Save the details using --> POST
	@PostMapping("/registerEmployee")
	public String addEmployee(Employee employee, @RequestParam("profilePic") MultipartFile file, RedirectAttributes rat )
	{
		adminService.addEmployee(employee,file);
		rat.addFlashAttribute("mssg","Employee Registered Successfully");
		return "redirect:/admin/addEmployee";  //admin->endpoint /addEmployee->endpoint
	}
	
//to provide Notice by Admin	
	@GetMapping("/addNotice")
	public String addNotice(Model model)
	{
		AdminNotice notice = new AdminNotice();
		model.addAttribute("noticeObj",notice);
	   return "admin/admin_notice"; // This should be a Thymeleaf or JSP view	
	}
	
// POST: Handle form submission
	@PostMapping("/postNotice")
	public String postNotice(@ModelAttribute("noticeObj") AdminNotice notice ,RedirectAttributes rat)
	{
	    adminService.addNotice(notice);
	    rat.addFlashAttribute("mssg","Notice Posted Successfully");
	    return "redirect:/admin/addNotice"; // Fixed missing slash
	}
	
	@GetMapping("/allUsers")
	public String allUsers(Model model)
	{
	 List<UserDetails> userList =	adminService.allUsers();
	 model.addAttribute("userInfo",userList);
		return "admin/all_users";
	}
	
	@GetMapping("/allEmployee")
	public String allEmployess(Model model)
	{
		List<Employee> empList = adminService.allEmployee();
		model.addAttribute("empKey",empList);
		return "admin/all_employee";
	}
	
	@GetMapping("/allContacts")
	public String AllContacts(Model model)
	{
		List<Contact> contList = adminService.allContacts();
		model.addAttribute("contactKey",contList);
		return "admin/all_contacts";
	}
	

	 
//delete User using Jpa-derived
	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam String email,Model model)
	{
		adminService.deleteUser(email);
		
		return  "redirect:/admin/allUsers";
	}
//
	//delete Employee using jpa-inbuilt-method	
	@PostMapping("/deleteEmployee/{empId}")
	public String deleteEmployee(@PathVariable("empId") String employee_id, RedirectAttributes rat )
	{
		adminService.deleteEmployee(employee_id);
		rat.addFlashAttribute("mssg","Employee deleted successfully.😊😊😊");
		return "redirect:/admin/allEmployee";
				
	}
	/* The URL has a parameter named empId You are receiving that value into a
	 * variable named employee_id*/
	
	//📝 Notes:
	//@PostMapping("/deleteEmployee/{empId}") maps to your form’s action URL.
	//@PathVariable("empId") String employee_id binds the {empId} from the URL.
	//like  String employee_id = request.getParameter("emp_id"); -> conceptually similar
	//But instead of form field, we’re extracting it from the URL path variable.

//delete contact 
		@GetMapping("/deleteContact")
		public String deleteContact(@RequestParam int contactId,Model model)
		{
			adminService.deleteContact(contactId);			
			return  "redirect:/admin/allContacts";
		}
			 /* User clicks Delete → sends GET request with contactId → Controller receives
			 * contactId via @RequestParam → Passes it to Service method → Service deletes
			 * the contact by ID
			 */

	
//edit data of user
	@GetMapping("/editUser")
	public String editUser(@RequestParam String email,Model model)
	{
	  UserDetails ud =	adminService.editUser(email);		
			model.addAttribute("userInfo",ud); 
			return "admin/edit_user";
	}

//edit user postMapp -->process
    @PostMapping("/editUser")
    public String editUserFinal(@ModelAttribute UserDetails modifiedUser) //modified value from edit-> html-form
    {
    	
    	adminService.editUserFinal(modifiedUser);
    	return "redirect:/admin/allUsers";
    }
    
//show employee data on edit form 
    @GetMapping("/editEmployee")
    public String editEmployee(@RequestParam("employeeId") String empId,Model model)
    {
    	
      Employee employee= adminService.editEmployee(empId);
      System.out.println("Id In getMethod:"+employee.getEmpId());
      model.addAttribute("empInfo",employee);
      return "admin/edit_employee"; 	
    }  
    //The URL generated is: /admin/editEmployee?employeeId=EMP123
    //@RequestParam("employeeId") binds employeeId to empId
    
//edit employee @PostMap process
    @PostMapping("/updateEmployee")
    public String editEmployeeFinal(@ModelAttribute Employee modifiedEmp)
    {
    	System.out.println("Controller id in PostMethod :-->"+modifiedEmp.getEmpId());
	       adminService.editEmployeeFinal(modifiedEmp);
	       return "redirect:/admin/allEmployee";
    }
    
//view all notice
    @GetMapping("/allNotice")
    public String allnotice(Model model)
    {
    	List<AdminNotice> noticeList =	adminService.allNotice();
   	    model.addAttribute("noticeKey",noticeList);   	   
   		return "admin/all_notice";
    }
	
	 /* --> If your controller is annotated with @Controller → return String (for views).
	  --> If it's @RestController → return data (List, Map, Object, etc. */
	
//delete notice using Query-Parameter
    @GetMapping("/deleteNotice")
    public String deleteNotice(@RequestParam int noticeId,Model model)
    {
    	adminService.deleteNotice(noticeId);
    	return "redirect:/admin/allNotice";
    }


    @GetMapping("/allBookings")
    public String allpendingBookings(Model model)
    {
    	List<BookingEmployee> bkE =adminService.allBookings();
    	System.out.println("booking is:"+bkE.getFirst());
    	
    	model.addAttribute("bookingKey",bkE);
    	return "admin/all_bookings";
    }
    
    @GetMapping("/respondBooking")
    public String showResponseForm(@RequestParam int bookingId,Model model)
    {
    	model.addAttribute("bookId",bookingId);
    	return "admin/booking_response";
    }
    @PostMapping("/respondBooking")
    public String doResponseBook(@RequestParam int id,
                                 @RequestParam String adminMessage)
    {
    	adminService.addBookingResponse(id,adminMessage);
    	return "redirect:/admin/allBookings";
    }
    
  
}
