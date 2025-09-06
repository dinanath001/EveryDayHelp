package com.portal.everyday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor //default constructor
public class AdminNotice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer noticeId;
	
	private String title;
	
	@Column(length = 1000 )
    private String description;
    
    //constructor
	public AdminNotice(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}
	
//@Data-->	You don't need @Getter and @Setter if you're already using @Data	
    

}
