package com.portal.everyday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portal.everyday.entity.BookingEmployee;

public interface BookingEmployeeRepository extends JpaRepository<BookingEmployee, Integer> {
	
	List<BookingEmployee> findByStatusFalseOrderByCreatedAtDesc();
	
	
	// Derived query to find all bookings by email
	List<BookingEmployee> findAllByEmail(String email);
	


	
}
