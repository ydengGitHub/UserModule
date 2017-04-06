package com.usermodule.util;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usermodule.dto.UserDetailsImpl;
import com.usermodule.entities.User;
import com.usermodule.repositories.UserRepository;
import com.usermodule.services.UserServiceImpl;

/*Include utility methods will be called from different place*/
@Component
public class MyUtil {
	
	private static MessageSource messageSource;
	private static final Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	private static String hostAndPort;
	
	//@Value("${spring.profiles.active}")//Have to use setMethod(), this doesn't work for static variable!
	private static String activeProfiles;
	
	/*Constructor to inject for static variable*/
	@Autowired
	public MyUtil(MessageSource messageSource){
		MyUtil.messageSource=messageSource;
	}
	
    @Value("${hostAndPort}")
    public void setHostAndPort(String hostAndPort) {
    	MyUtil.hostAndPort = hostAndPort;
    }
	
    @Value("${spring.profiles.active}")
    public void setActiveProfiles(String activeProfiles) {
    	MyUtil.activeProfiles = activeProfiles;
    }
    
	public static boolean isDev(){
		return activeProfiles.equals("dev");
	}
	
	public static String hostUrl(){
		return (isDev()?"http://": "https://")+hostAndPort;
	}
	
	
	public static void flash(RedirectAttributes redirectAttributes, String kind, String messageKey){
		redirectAttributes.addFlashAttribute("flashKind", kind);
		redirectAttributes.addFlashAttribute("flashMessage", MyUtil.getMessage(messageKey));
	}

	/*Get the message from "messages.properties"*/
	public static String getMessage(String messageKey, Object... args) {
		return messageSource.getMessage(messageKey, args, Locale.getDefault());
	}

	public static void validate(boolean valid, String msgContent, Object... args) {
		if(!valid) throw new RuntimeException(getMessage(msgContent, args));
		
	}

	public static User getSessionUser() {
		UserDetailsImpl auth=getAuth();
		return auth == null?null:auth.getUser();
	}
	
	public static UserDetailsImpl getAuth(){
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		
		if(auth !=null){
			Object principal=auth.getPrincipal();
			if(principal instanceof UserDetailsImpl){
				return (UserDetailsImpl) principal;
			}
		}
		return null;
	}
}
