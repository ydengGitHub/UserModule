package com.usermodule.controllers;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usermodule.dto.ForgotPasswordForm;
import com.usermodule.dto.ResetPasswordForm;
import com.usermodule.dto.SignupForm;
import com.usermodule.mail.MailSender;
import com.usermodule.services.UserService;
import com.usermodule.util.MyUtil;
import com.usermodule.validators.ForgotPasswordFormValidator;
import com.usermodule.validators.ResetPasswordFormValidator;
import com.usermodule.validators.SignupFormValidator;

@Controller
public class RootController {

	private static final Logger logger = LoggerFactory.getLogger(RootController.class);
	
	private MailSender mailSender;
	private UserService userService;
	private SignupFormValidator signupFormValidator;
	private ForgotPasswordFormValidator forgotPasswordFormValidator;
	private ResetPasswordFormValidator resetPasswordFormValidator;

	
	@Autowired
	public RootController(MailSender mailSender, UserService userService,
			@Qualifier("signupFormValidator") SignupFormValidator signupFormValidator,
			@Qualifier("forgotPasswordFormValidator") ForgotPasswordFormValidator forgotPasswordFormValidator,
			@Qualifier("resetPasswordFormValidator") ResetPasswordFormValidator resetPasswordFormValidator) {
		this.mailSender = mailSender;
		this.userService = userService;
		this.signupFormValidator = signupFormValidator;
		this.forgotPasswordFormValidator = forgotPasswordFormValidator;
		this.resetPasswordFormValidator = resetPasswordFormValidator;
		
	}
	
	@InitBinder("signupForm")
	protected void initSignupBinder(WebDataBinder binder) {
		binder.setValidator(signupFormValidator);
	}
	
	@InitBinder("forgotPasswordForm")
	protected void initForgotPasswordBinder(WebDataBinder binder) {
		binder.setValidator(forgotPasswordFormValidator);
	}
	
	@InitBinder("resetPasswordForm")
	protected void initResetPasswordBinder(WebDataBinder binder) {
		binder.setValidator(resetPasswordFormValidator);
	}
	
	/* For Get method only */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		// model.addAttribute("name", "Yan"); //(attributeName, attributeValue)
		// model.addAttribute("signupForm", new SignupForm());
		model.addAttribute(new SignupForm()); // same as Above, signupForm will
												// be the default key
		return "signup";
	}

	/* For Post method, to handle the submitted form on the page */
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
	
	/*Forgot Password requests*/
	@RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	public String forgotPassword(Model model) {
		model.addAttribute(new ForgotPasswordForm()); // same as Above, signupForm will
												// be the default key
		return "forgot-password";
	}
	
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
	public String forgotPassword(
			@ModelAttribute("forgotPasswordForm") @Valid ForgotPasswordForm forgotPasswordForm,
			BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors())
			return "forgot-password";

		userService.forgotPassword(forgotPasswordForm);
		MyUtil.flash(redirectAttributes, "info", "checkMailResetPassword");

		return "redirect:/";
	}
	
    /**
     * Reset password request
     */
    @RequestMapping(value = "/reset-password/{forgotPasswordCode}", method = RequestMethod.GET)
    public String resetPassword(@PathVariable("forgotPasswordCode") String forgotPasswordCode, Model model) {
    	
     	model.addAttribute(new ResetPasswordForm());
    	return "reset-password";
    	
    }
    
	@RequestMapping(value = "/reset-password/{forgotPasswordCode}",
			method = RequestMethod.POST)
	public String resetPassword(
			@PathVariable("forgotPasswordCode") String forgotPasswordCode,
			@ModelAttribute("resetPasswordForm")
				@Valid ResetPasswordForm resetPasswordForm,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		userService.resetPassword(forgotPasswordCode, resetPasswordForm, result);
		
		if (result.hasErrors())
			return "reset-password";

		MyUtil.flash(redirectAttributes, "success", "passwordChanged");

		return "redirect:/login";
	}

}
