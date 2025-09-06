package com.portal.everyday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portal.everyday.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	void deleteByContactId(int contactId);

}
