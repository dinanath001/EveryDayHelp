package com.portal.everyday.service;


import org.springframework.stereotype.Service;

import com.portal.everyday.entity.Contact;
import com.portal.everyday.repository.ContactRepository;

@Service
public class PublicService {
	
	private  ContactRepository contactRepository;
	public PublicService(ContactRepository contactRepository) //constructor Injection
	{
		this.contactRepository = contactRepository;
	}
	
	public void addContact(Contact contact)
	{
		// TODO Auto-generated method stub 
		 Contact cnt= contactRepository.save(contact);	
	}

}
