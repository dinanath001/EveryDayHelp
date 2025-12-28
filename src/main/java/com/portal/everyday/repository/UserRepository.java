package com.portal.everyday.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import com.portal.everyday.entity.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, String> {
	
	  // ✅ Already present
	// Jpa Derived method for checking email&password for get login the usr after verification 
	public UserDetails findByEmailAndPassword(String email, String password);
	
    public void deleteByEmail(String email); //derived method for deleting user by Admin
    
    public UserDetails findByEmail(String email); //for edit user by admin/User -->identify the User in D/B

  
    

    // ✅ Newly added for Forgot Password
    Optional<UserDetails> findByResetToken(String resetToken);  // find user by reset token
	//Using Optional is safer — it prevents NullPointerException if no user found.

}
