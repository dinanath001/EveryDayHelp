package com.portal.everyday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.portal.everyday.service.MailTest;



@SpringBootApplication
public class EveryDayHelpApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(EveryDayHelpApplication.class, args);
	
	}

@Autowired	
private MailTest mailTest;	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//mailTest.sendTestMail();
		
	}

}
