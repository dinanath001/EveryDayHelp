package com.portal.everyday.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "feedback")
public class FeedBack {
       
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private long id;
	 
	 private String name;
	 private String employeeName;
	 private String remark; 
	 private int rating;
	 private String Type;
	 private String serviceType;
	 
	 @ManyToOne
	 @JoinColumn(name = "user_email", referencedColumnName = "email")
	 private UserDetails user;
	 
	 @CreationTimestamp
	 private LocalDateTime createdAt;

	public FeedBack() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedBack(String remark, int rating, String serviceType, UserDetails user, LocalDateTime createdAt) {
		super();
		this.remark = remark;
		this.rating = rating;
		this.serviceType = serviceType;
		this.user = user;
		this.createdAt = createdAt;
	}
	 
	 
	 
}
