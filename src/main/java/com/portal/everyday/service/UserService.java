package com.portal.everyday.service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import com.portal.everyday.xhelper.FileUploadUtil;

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
	    String db_path = oldUser.getPic();
		try {
			if(updatedPic != null && !updatedPic.isEmpty())
			{
				String oldImagePath = oldUser.getPic();
			    if (oldImagePath != null && !oldImagePath.contains("default")) {
			        File oldFile = new File(System.getProperty("user.dir"), oldImagePath);
			        if (oldFile.exists())
			        	{
			        	  oldFile.delete();
			        	}
			    }
			    //call image save utility method from FileUploadUtil.java
				db_path = FileUploadUtil.saveFile(updatedPic, "user");
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("❌ Image upload failed — keeping old image path.");
		}
	    oldUser.setPic(db_path);
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




	   @Autowired
	   private JavaMailSender mailSender;
	//1
		    public void generateResetToken(String email) {
		        UserDetails userOpt = userRepository.findByEmail(email);
		        if (userOpt != null) {
		       
		            String token = UUID.randomUUID().toString();
		            userOpt.setResetToken(token);
		            userOpt.setTokenExpiry(LocalDateTime.now().plusMinutes(15));
		            userRepository.save(userOpt);

		            // send reset link
		            sendResetEmail(userOpt.getEmail(), token);
		        }
		    }

	//2	    
		    private void sendResetEmail(String email, String token) {
		        String link = "http://localhost:8080/user/reset-password?token=" + token;

		        SimpleMailMessage message = new SimpleMailMessage();
		        message.setTo(email);
		        message.setSubject("Password Reset Request");
		        message.setText("Click the link to reset your password: " + link);

		        mailSender.send(message);
		    }

	//3	    
		    public boolean resetPassword(String token, String newPassword) {
		        Optional<UserDetails> userOpt = userRepository.findByResetToken(token);
		        if (userOpt.isPresent()) {
		            UserDetails user = userOpt.get();

		            // check expiry
		            if (user.getTokenExpiry().isAfter(LocalDateTime.now())) {
		                user.setPassword(newPassword); // encode before saving
		                user.setResetToken(null);
		                user.setTokenExpiry(null);
		                userRepository.save(user);
		                return true;
		            }
		        }
		        return false;
		    }
		





}
