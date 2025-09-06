package com.portal.everyday.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="BookingEmployee")
public class BookingEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String serviceType;

    private String employeeType;
    private String email;
    
    @Column(length = 1000)
    private String userMessage;
    private String adminMessage;

    private boolean status = false;
    
    @CreationTimestamp
	 private LocalDateTime createdAt;

	

//no args constructor is not needed cuz of //@NoArgsConstructor(lombok)
    
    // All-args constructor
    public BookingEmployee(String serviceType, String employeeType, String email, String userMessage,
			String adminMessage, boolean status, LocalDateTime createdAt) {
		super();
		this.serviceType = serviceType;
		this.employeeType = employeeType;
		this.email = email;
		this.userMessage = userMessage;
		this.adminMessage = adminMessage;
		this.status = status;
		this.createdAt = createdAt;
	}
    
    
// Getters and Setters
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getServiceType() {
		return serviceType;
	}



	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}



	public String getEmployeeType() {
		return employeeType;
	}



	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getUserMessage() {
		return userMessage;
	}



	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}



	public String getAdminMessage() {
		return adminMessage;
	}



	public void setAdminMessage(String adminMessage) {
		this.adminMessage = adminMessage;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}



	

   
     
	
}
