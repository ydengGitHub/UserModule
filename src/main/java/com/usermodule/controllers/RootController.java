package com.usermodule.controllers;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usermodule.dto.SignupForm;
import com.usermodule.mail.MailSender;
import com.usermodule.services.UserService;
import com.usermodule.util.MyUtil;

@Controller
public class RootController {

	private MailSender mailSender;
	private static final Logger logger = LoggerFactory.getLogger(RootController.class);
	private UserService userService;

	@Value("${mail.receiver.email}")
	private String receiverEmail;

	@Autowired
	public RootController(MailSender mailSender, UserService userService) {
		this.mailSender = mailSender;
		this.userService=userService;
	}

	// @RequestMapping("/")
	// public String home() throws MessagingException{
	// //mailSender.send(receiverEmail, "Hello, world", "Mail from Yan.");
	// return "home";
	// }
	/* For Get method only */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		// model.addAttribute("name", "Yan"); //(attributeName, attributeValue)
		// model.addAttribute("signupForm", new SignupForm());
		model.addAttribute(new SignupForm()); // same as Above, signupForm will
												// be the default key
		return "signup";
	}

	/* For Post method */
	/* BindingResult will hold the error with input*/
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, 
			BindingResult result, RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) return "signup";
		//logger.info(signupForm.toString());
		/*create a service layer, and call the layer to add entry to the database*/
		userService.signup(signupForm);
		MyUtil.flash(redirectAttributes, "success", "signupSuccess");
		return "redirect:/"; // redirect to home page, with prefix "redirect:" ,
								// considered as an url but not the name of a
								// view
	}
}
