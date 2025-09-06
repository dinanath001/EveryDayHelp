package com.portal.everyday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portal.everyday.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

	
	
//	derived method for deleting Employee Object/data by Admin Dashboard;
	public void deleteByEmpId(String empId);

	public Employee findByEmpId(String empId);//to identify the Employee nd edit the employee via Admin panel
    
	
}
