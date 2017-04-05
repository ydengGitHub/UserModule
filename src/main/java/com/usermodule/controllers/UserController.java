package com.usermodule.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usermodule.services.UserService;
import com.usermodule.util.MyUtil;

/*Handle all requests start with /user*/
@Controller
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService=userService;
	}
	
	/*Read a path variable from a url, Spring will create the request object for us*/
	@RequestMapping("/users/{verificationCode}/verify")
	public String verify(@PathVariable("verificationCode") String verificationCode,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		userService.verify(verificationCode);
		MyUtil.flash(redirectAttributes, "success", "verificationSuccess");
		request.logout();
		return "redirect:/";
	}
}
