package com.usermodule.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*Include utility methods will be called from different place*/
@Component
public class MyUtil {
	
	private static MessageSource messageSource;
	
	/*Constructor to inject for static variable*/
	@Autowired
	public MyUtil(MessageSource messageSource){
		MyUtil.messageSource=messageSource;
	}
	
	public static void flash(RedirectAttributes redirectAttributes, String kind, String messageKey){
		redirectAttributes.addFlashAttribute("flashKind", kind);
		redirectAttributes.addFlashAttribute("flashMessage", MyUtil.getMessage(messageKey));
	}

	/*Get the message from "messages.properties"*/
	private static String getMessage(String messageKey, Object... args) {
		return messageSource.getMessage(messageKey, args, Locale.getDefault());
	}
	
	
}
