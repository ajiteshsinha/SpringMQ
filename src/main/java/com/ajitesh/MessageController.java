package com.ajitesh;

import java.io.Serializable;

import javax.jms.JMSException;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajitesh.models.Email;

@RestController
@RequestMapping("/messages")
public class MessageController {
	
	
	private static Logger log  = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	JmsMessagingTemplate jmsMessagingTemplate;
	
	@Autowired
	MessageConverter jacksonJmsMessageConverter;
	

	@GetMapping("/sendandreceive")
	public Email sendAneReceive() {
		
		Email email = new Email("info@example.com", "Hello from controller");
		
		 jmsTemplate.convertAndSend("mailbox", email, message -> { 
			 message.setJMSCorrelationID("33333333");
			 return message;
		 });
		
	//	jmsTemplate.sendAndReceive("mailbox", session -> session.createObjectMessage(email));
	//	Message message = jmsTemplate.sendAndReceive("mailbox", session -> jacksonJmsMessageConverter.toMessage(email, session));
	
	//	 Email emailRec = 	jmsMessagingTemplate.convertSendAndReceive("mailbox", email, Email.class);
		 
		 String resSelectorId = "JMSCorrelationID = '"+33333333+"'";
		 ActiveMQTextMessage message = (ActiveMQTextMessage)jmsTemplate.receiveSelected("mailbox", resSelectorId);
		 
//		 Email emailRec = (Email) jmsTemplate.receiveAndConvert("mailbox");
//		
//		 log.info("Response message :" + emailRec);
//		 emailRec = (Email) jmsTemplate.receiveAndConvert("mailbox");
//		 log.info("Response message :" + emailRec);
//	//	 emailRec = (Email) jmsTemplate.receiveAndConvert("mailbox");
	//	 log.info("Response message :" + emailRec);
		try {
			log.info("CorelationID = " + message.getJMSCorrelationID());
			log.info("Response = " + message.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		return email;
	}
}
