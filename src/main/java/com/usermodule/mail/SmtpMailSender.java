package com.usermodule.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SmtpMailSender implements MailSender {
	private static final Logger logger=LoggerFactory.getLogger(MockMailSender.class);
	
	private JavaMailSender javaMailSender;
	
	public void setJavaMailSender(JavaMailSender javaMailSender){
		this.javaMailSender=javaMailSender;
	}
	
	@Override
	public void send(String to, String subject, String body) throws MessagingException{
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		helper=new MimeMessageHelper(message, true);//true indicates multipart message
		helper.setSubject(subject);
		helper.setTo(to);
		helper.setText(body, true);//true indicates html
		javaMailSender.send(message);
	}
}
