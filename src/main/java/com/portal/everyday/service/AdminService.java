package com.portal.everyday.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portal.everyday.entity.Admin;
import com.portal.everyday.entity.AdminNotice;
import com.portal.everyday.entity.BookingEmployee;
import com.portal.everyday.entity.Contact;
import com.portal.everyday.entity.Employee;
import com.portal.everyday.entity.FeedBack;
import com.portal.everyday.entity.UserDetails;
import com.portal.everyday.repository.AdminNoticeRepository;
import com.portal.everyday.repository.AdminRepository;
import com.portal.everyday.repository.BookingEmployeeRepository;
import com.portal.everyday.repository.ContactRepository;
import com.portal.everyday.repository.EmployeeRepository;
import com.portal.everyday.repository.UserRepository;

import jakarta.transaction.Transactional;



@Service
public class AdminService {
	@Autowired 
	private  AdminNoticeRepository noticeRepository; //Field Injection
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	private final EmployeeRepository employeeRepository;
	private final AdminRepository adminRepository;
	public AdminService(AdminRepository adminRepository,EmployeeRepository employeeRepository)
	{
		this.adminRepository = adminRepository;
		this.employeeRepository= employeeRepository;
	} //constructor dependency injection
	

//Add Employee by Admin with their Profile Picture	
    public void addEmployee(Employee employee, MultipartFile file) {
        try {
            // Get the root path of the project
            String projectRoot = System.getProperty("user.dir");
            System.out.println("Project root is: " + projectRoot);

            // Define the folder name for uploads
            String folderName = "uploads/employee";

            // Construct the full path for the upload directory
            String uploadPath = projectRoot + File.separator + folderName;
            System.out.println("Upload path is: " + uploadPath);

            // Create the upload directory if it doesn't exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // creates the directory
                System.out.println("Created upload folder at: " + uploadDir.getAbsolutePath());
            }

            // Get original filename from the uploaded file
            String originalFilename = file.getOriginalFilename();
            System.out.println("Original filename is: " + originalFilename);

            // Generate a unique filename to avoid overwrite
           String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;

            // Create the destination file object
            File destinationFile = new File(uploadDir, uniqueFilename);

            // Save the uploaded file to the destination
            file.transferTo(destinationFile);
            System.out.println("Image saved to: " + destinationFile.getAbsolutePath());

            // Save the relative file path in the database
            String dbPath = folderName + "/" + uniqueFilename;
            employee.setPic(dbPath);

            // Save the employee data to the database
            employeeRepository.save(employee);

        } catch (IOException e) {
            System.out.println(" Employee Image Upload Failed");
            e.printStackTrace();
        }
    }
    
    //Using Global File Uploader
//  public void addEmployee(Employee employee, MultipartFile file) {
//      try {
//          // Use the utility to save the uploaded file
//          // This returns the relative DB path like: uploads/employee/1718712345678_photo.jpg
//          String imagePath = FileUploadUtil.saveFile(file, "employee");
//
//          // Set the image path in the employee object
//          employee.setPic(imagePath);
//
//          // Save employee data to the database
//          employeeRepository.save(employee);
//
//      } catch (Exception e) {
//          // Print the exception if something goes wrong
//          e.printStackTrace();
//      }
//  }

//Add Notice by Admin     
	public void addNotice(AdminNotice notice)
	{
		// TODO Auto-generated method stub
		AdminNotice adnt = noticeRepository.save(notice);
		
	}


	public List<UserDetails> allUsers()
	{
       List<UserDetails> userList = userRepository.findAll();
	    return userList;
	}


	public List<Employee> allEmployee() {
		List<Employee> empList = employeeRepository.findAll();
		return empList;
	}


	public List<Contact> allContacts() {
	    List<Contact> contactList = contactRepository.findAll();
	    return contactList;
	}

//Admin login process using Jpa-Derived Method
	public Admin adminLogin(String email, String pass)
	{
	   Admin admin =	adminRepository.findByEmailAndPassword(email, pass);
		return admin;
		// TODO Auto-generated method stub	    
	}


	// ==============================
	// Delete User by Email (Derived Method)
	// ==============================
	@Transactional // Commit or rollback is required for consistency during delete/update
	public void deleteUser(String email) {
	    userRepository.deleteByEmail(email);  // Derived method: deletes where email = ?
	}


	// ==============================
	// Delete Employee by empId (Derived Method or Built-in)
	// ==============================
	@Transactional // Recommended here too to ensure data integrity
	public void deleteEmployee(String employee_id) {
	    employeeRepository.deleteByEmpId(employee_id); // Derived method (if empId is not @Id)
	    // Alternatively: employeeRepository.deleteById(employee_id); // if empId is @Id
	}


	// ==============================
	// Delete Contact by contactId (Derived or Built-in)
	// ==============================
	@Transactional
	public void deleteContact(int contactId) {
	    contactRepository.deleteByContactId(contactId); // Derived method
	    // Alternatively: contactRepository.deleteById(contactId); // if contactId is @Id
	}



//edit user method (for Getmapping)
	public UserDetails editUser(String email)
	{	
	  return userRepository.findByEmail(email);
	}

//for Post method
	public void editUserFinal(UserDetails modifiedUser) {
		// TODO Auto-generated method stub
		String email =modifiedUser.getEmail();
		UserDetails oldUser =  userRepository.findByEmail(email);
		oldUser.setName(modifiedUser.getName());
		oldUser.setPhone(modifiedUser.getPhone());
		oldUser.setAddress(modifiedUser.getAddress());
		userRepository.save(oldUser);
	}

//show edit form nd data -->then--> find employee by derived method	
	public Employee editEmployee(String empId) {
		// TODO Auto-generated method stub
		Employee emp= employeeRepository.findByEmpId(empId);
		return emp;
	}
	
	
//for @Post process...
	public void editEmployeeFinal(Employee modifiedEmp)
	{    
		 System.out.println("id in Service:-->"+modifiedEmp.getEmpId());
		 
         String empId = modifiedEmp.getEmpId();
         Employee oldEmp = employeeRepository.findByEmpId(empId);
         System.out.println("old emp:"+oldEmp);
         oldEmp.setName(modifiedEmp.getName());
         oldEmp.setPhone(modifiedEmp.getPhone());
         oldEmp.setQualification(modifiedEmp.getQualification());
         oldEmp.setExperience(modifiedEmp.getExperience());
         //now save the updated data in D~Base
           employeeRepository.save(oldEmp);    
           
	}
	//edit employee using jdbcTemplate
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	public void editEmployeeFinal(Employee modifiedEmp) {
//	    String updateQuery = "UPDATE employee SET name = ?, phone = ?, qualification = ?, experience = ? WHERE emp_id = ?";
//
//	    int rowsAffected = jdbcTemplate.update(updateQuery,
//	            modifiedEmp.getName(),
//	            modifiedEmp.getPhone(),
//	            modifiedEmp.getQualification(),
//	            modifiedEmp.getExperience(),
//	            modifiedEmp.getEmpId()
//	    );
//
//	    System.out.println("Updated rows: " + rowsAffected);
//	}


	
	
//all Notice to admin	
	public List<AdminNotice> allNotice()
	{
	  List<AdminNotice> nList= noticeRepository.findAll();
	  return nList;
	}
	
//delete notice via JdbcTemplate
@Autowired
JdbcTemplate jdbcTemplate;

	public void deleteNotice(int noticeId) {
		
	  String deleteQuery = "DELETE  FROM admin_notice WHERE notice_id = ?";
	  jdbcTemplate.update(deleteQuery,noticeId);
	  
	}

//viewing all pending bookings /by Jpa Derived method 
@Autowired
private BookingEmployeeRepository bookingEmployeeRepository;

	public List<BookingEmployee> allBookings() {
		// TODO Auto-generated method stub
	   List<BookingEmployee> bke = bookingEmployeeRepository.findByStatusFalseOrderByCreatedAtDesc();
	 return bke;
		  
	}


	public void addBookingResponse(int id, String adminMessagee) {
		// TODO Auto-generated method stub
		BookingEmployee bke = bookingEmployeeRepository.findById(id).get();
		bke.setAdminMessage(adminMessagee);
		bke.setStatus(true);
		bookingEmployeeRepository.save(bke);
		
	}










    
    
    
    
    
    
    
    
   
    
    
    
    
    
    


    


	
	
	
     

}
