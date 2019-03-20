package com.ajitesh.jms.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.ajitesh.models.Email;

@Component
public class EmailListener {
	
	private static Logger log  = LoggerFactory.getLogger(EmailListener.class);
	
//	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	public void receiveEmail(Email email) {
		log.info("Message received : " + email);		
	}

}
