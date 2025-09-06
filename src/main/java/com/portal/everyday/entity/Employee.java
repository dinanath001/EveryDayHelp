package com.portal.everyday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name="employee")
public class Employee {
	
	@Id
	private String empId;
	
	private String name;
	
	private String phone;
	
	private String qualification;
	
	private String experience;
	
	private String gender; // male /female /radio
	
	private String employee_type; //dropdown
	 
	private String service_type; // LTS / STS //radio
	 
	private String charges;
	
	@Lob
	private String description; //text area /This will use a large object field (TEXT/CLOB)
	
	private String pic;

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(String empId, String name, String phone, String qualification, String experience, String gender,
			String employee_type, String service_type, String charges, String description, String pic) {
		super();
		this.empId = empId;
		this.name = name;
		this.phone = phone;
		this.qualification = qualification;
		this.experience = experience;
		this.gender = gender;
		this.employee_type = employee_type;
		this.service_type = service_type;
		this.charges = charges;
		this.description = description;
		this.pic = pic;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmployee_type() {
		return employee_type;
	}

	public void setEmployee_type(String employee_type) {
		this.employee_type = employee_type;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	

	
	

}
