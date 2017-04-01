package com.usermodule.controllers;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.usermodule.mail.MailSender;

@Controller
public class RootController {
	
	private MailSender mailSender;
	
	@Value("${mail.receiver.email}")
	private String receiverEmail;
	
	@Autowired
	public RootController(MailSender mailSender){
		this.mailSender=mailSender;
	}
	
//	@RequestMapping("/")
//	public String home() throws MessagingException{
//		//mailSender.send(receiverEmail, "Hello, world", "Mail from Yan.");
//		return "home";
//	}	
	
}
