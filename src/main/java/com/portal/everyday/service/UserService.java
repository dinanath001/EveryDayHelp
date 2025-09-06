package com.portal.everyday.service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portal.everyday.entity.BookingEmployee;
import com.portal.everyday.entity.Employee;
import com.portal.everyday.entity.FeedBack;
import com.portal.everyday.entity.UserDetails;
import com.portal.everyday.repository.BookingEmployeeRepository;
import com.portal.everyday.repository.EmployeeRepository;
import com.portal.everyday.repository.FeedbackRepository;
import com.portal.everyday.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final FeedbackRepository feedbackRepository;
	private final EmployeeRepository employeeRepository;
	private final BCryptPasswordEncoder passwordEncoder; //password encoding
	public UserService(UserRepository userRepository, FeedbackRepository feedbackRepository, EmployeeRepository employeeRepository) 
	{
		this.userRepository = userRepository;
		this.employeeRepository = employeeRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.feedbackRepository = feedbackRepository;
	}
	

	


//User registration
	public void addUser(UserDetails userDetails, MultipartFile file)
	{
		try {
			String projectRoot = System.getProperty("user.dir");
					System.out.println("Project root is: "+ projectRoot);
					
			//define folder name inside project
					String folderName = "uploads/user";
			
			//Construct full path for the upload directory
					String uploadPath  = projectRoot + File.separator + folderName;
					System.out.println("Upload Path is: "+ uploadPath);
					
			//Create the upload directory if it doesn't exist
					File uploadDir = new File(uploadPath);
					
					if(!uploadDir.exists())
					{
						uploadDir.mkdirs(); //creates parent directory if needed
						System.out.println("Created Upload folder at: "+uploadDir.getAbsolutePath());
					}
			//Get uploaded file name from the multipart	
					String originalFilename = file.getOriginalFilename();
					System.out.println("File name is: "+originalFilename);
			
			String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
			
			//create the Desitinantion file
			File destinationFIle = new File(uploadDir , uniqueFilename);
			
			//transfer the file from multipartFile to the destination
			file.transferTo(destinationFIle);
			System.out.println("Images saved to: "+ destinationFIle.getAbsolutePath());
			
			//save relative path(folderName/fileName) to Database
			String DB_PATH = folderName + "/" +uniqueFilename;
			userDetails.setPic(DB_PATH);		
			
//			//Encryption Password using -->BCryptPasswordEncoder passwordEncoder
//			String encryptedPassword = passwordEncoder.encode(userDetails.getPassword());
//			System.out.println("Password after encryption: "+encryptedPassword);
//			userDetails.setPassword(encryptedPassword);
			UserDetails ud = userRepository.save(userDetails);		
			
		}
		catch (IOException e)
		{
			System.out.println("Image Upload Failed");
			//e.getMessage();
     		e.printStackTrace();
		}
		
	}

//user Login process via JPA-Repository
//	public UserDetails userLogin(String email, String pass) {
//		// TODO Auto-generated method stub
//		UserDetails ud = userRepository.findByEmailAndPassword(email, pass);
//		return ud;
//	}

	
//Login module using JdbcTemplate	
@Autowired	
private JdbcTemplate template;

	public UserDetails userLogin(String email, String pass) {
		UserDetails user = null;
		//
		String sql = "SELECT * FROM user_details WHERE email=? and password=? ";
		try {
			    user =  template.queryForObject(sql, new RowMapper<UserDetails>() {
//email, address, name, password, phone, pic
				@Override
				public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserDetails ud = new UserDetails();
					 ud.setAddress(rs.getString("address"));
					 ud.setEmail(rs.getString("email"));
					 ud.setPassword(rs.getString("password"));
					 ud.setName(rs.getString("name"));
					 ud.setPhone(rs.getString("phone"));
					 ud.setPic(rs.getString("pic"));
					
					 return ud;
				}		   
			}, email,pass);	 
			    
		}
		catch (EmptyResultDataAccessException erd)
		{
			erd.printStackTrace();
		}
      return user;	
	}


//edit profile method + image handled /	
	public UserDetails updateUser(UserDetails updatedUser, MultipartFile updatedPic) {
	    String email = updatedUser.getEmail();
	    System.out.println("Updating user with email: " + email);

	    UserDetails oldUser = userRepository.findByEmail(email);

	    // Update basic info
	    oldUser.setName(updatedUser.getName());
	    oldUser.setPhone(updatedUser.getPhone());
	    oldUser.setAddress(updatedUser.getAddress());

	    // Image update logic
	    if (updatedPic != null && !updatedPic.isEmpty()) {
	        try {
	            // Project root
	            String projectRoot = System.getProperty("user.dir");
	            System.out.println("Project root is:-> " + projectRoot);

	            // Folder setup
	            String folderName = "uploads/user";
	            String uploadPath = projectRoot + File.separator + folderName;
	            System.out.println("Upload path is: " + uploadPath);

	            File uploadDir = new File(uploadPath);
	            if (!uploadDir.exists()) { // ✅ Fix here
	                uploadDir.mkdirs();
	                System.out.println("Created upload folder at: " + uploadDir.getAbsolutePath());
	            }

	            // Delete old image if it exists and is not default
	            String oldImagePath = oldUser.getPic();
	            if (oldImagePath != null && !oldImagePath.contains("default")) {
	                File oldFile = new File(projectRoot + File.separator + oldImagePath);
	                if (oldFile.exists()) {
	                    oldFile.delete();
	                    System.out.println("Deleted old image: " + oldFile.getAbsolutePath());
	                }
	            }

	            // Save new image
	            String originalFileName = updatedPic.getOriginalFilename();
	            System.out.println("Original file name is: " + originalFileName);

	            String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
	            File destinationFile = new File(uploadDir, uniqueFileName);

	            updatedPic.transferTo(destinationFile);
	            System.out.println("Image is saved to: " + destinationFile.getAbsolutePath());

	            // Save relative path in DB
	            String DB_PATH = folderName + "/" + uniqueFileName;
	            oldUser.setPic(DB_PATH);

	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Image Upload Failed");
	        }
	    }
	    userRepository.save(oldUser);
	    return oldUser;
	}




	public void saveFeedback(FeedBack feedback, String email) {
		// TODO Auto-generated method stub
		UserDetails user = userRepository.findByEmail(email);
		feedback.setUser(user);
		feedbackRepository.save(feedback);
		
	}


	public Employee employeeInfo(String service_type, String employee_type) {
		// TODO Auto-generated method stub
		Employee emp = null;
		//id, created_at, employee_name, employee_type, name, rating, remark, service_type, user_email, type
		String query = "SELECT * FROM employee WHERE service_type=? and employee_type=? LIMIT 1 ";
		try {
			  emp =  template.queryForObject(query, new RowMapper<Employee>() {

					@Override
					public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
					    Employee empl = new Employee();
					    empl.setCharges(rs.getString("charges"));
					    empl.setDescription(rs.getString("description"));
					    empl.setService_type(rs.getString("service_type"));
					    empl.setEmployee_type(rs.getString("employee_type"));
					    
					    return empl;
					}
			    	
			    },service_type,employee_type);
		}
		catch (EmptyResultDataAccessException ed) {
			ed.printStackTrace();
		}
		return emp;
	}


	

	
@Autowired
private BookingEmployeeRepository bookingEmpRepository;
private static final Logger logger = LoggerFactory.getLogger(UserService.class);

//for saving Booking Details -->postMap -process
	public void saveEmpBooking(BookingEmployee bookingEmployee)
	{
		// TODO Auto-generated method stub
		  bookingEmpRepository.save(bookingEmployee);
	      logger.info("✅ Booking saved for: {}", bookingEmployee.getEmail());		 
			  
	}





	public List<BookingEmployee> bookingStatus(String email) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM booking_employee WHERE email = ? AND status = true ORDER BY created_at ASC";
		
	   List<BookingEmployee> bkList = template.query(query, new RowMapper<BookingEmployee>() {
//id, admin_message, created_at, email, employee_type, service_type, status, user_message
			@Override
			public BookingEmployee mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				BookingEmployee bk = new BookingEmployee();
				bk.setServiceType(rs.getString("service_type"));
				bk.setEmployeeType(rs.getString("employee_type"));
				bk.setAdminMessage(rs.getString("admin_message"));
				bk.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				bk.setStatus(rs.getBoolean("status"));
				return bk;
			}
        	  
          },email);
	   
	   return bkList;	
	}







}
