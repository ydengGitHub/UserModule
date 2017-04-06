package com.usermodule.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usermodule.dto.ChangeEmailForm;
import com.usermodule.dto.ChangePasswordForm;
import com.usermodule.dto.UserEditForm;
import com.usermodule.entities.User;
import com.usermodule.services.UserService;
import com.usermodule.util.MyUtil;
import com.usermodule.validators.ChangeEmailFormValidator;
import com.usermodule.validators.ChangePasswordFormValidator;

/*Handle all requests start with /user, handle common prefix*/
@Controller
@RequestMapping("/users")
public class UserController {
	
	private UserService userService;
	private ChangePasswordFormValidator changePasswordFormValidator;
	private ChangeEmailFormValidator changeEmailFormValidator;
	
	@Autowired
	public void setUserService(UserService userService, 
			@Qualifier("changePasswordFormValidator") ChangePasswordFormValidator changePasswordFormValidator,
			@Qualifier("changeEmailFormValidator") ChangeEmailFormValidator changeEmailFormValidator){
		this.userService=userService;
		this.changePasswordFormValidator=changePasswordFormValidator;
	}
	
	@InitBinder("changePasswordForm")
	protected void initChangePasswordBinder(WebDataBinder binder) {
		binder.setValidator(changePasswordFormValidator);
	}
	
	@InitBinder("changeEmailForm")
	protected void initChangeEmailBinder(WebDataBinder binder) {
		binder.setValidator(changeEmailFormValidator);
	}
	
	/* Verify the account.
	 * Read a path variable from a url, Spring will create the request object for us*/
	@RequestMapping("/{verificationCode}/verify")
	public String verify(@PathVariable("verificationCode") String verificationCode,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		userService.verify(verificationCode);
		MyUtil.flash(redirectAttributes, "success", "verificationSuccess");
		request.logout();
		return "redirect:/";
	}
	
	@RequestMapping(value="/{userId}")
	public String getById(@PathVariable("userId") long userId, Model model){
		model.addAttribute("user", userService.findOne(userId));
		return "user";
	}
	
	/*Profile edit request*/
	@RequestMapping(value="/{userId}/edit")
	public String edit(@PathVariable("userId") long userId, Model model){
		User user=userService.findOne(userId);
		UserEditForm form=new UserEditForm();
		form.setName(user.getName());
		form.setRoles(user.getRoles());
		model.addAttribute(form);
		return "user-edit";
	}
	
	@RequestMapping(value="/{userId}/edit", method=RequestMethod.POST)
	public String edit(@PathVariable("userId") long userId,
			@ModelAttribute("userEditForm") @Valid UserEditForm userEditForm,
			BindingResult result, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		
		if(result.hasErrors()) return "user-edit";
		
		userService.update(userId, userEditForm);
		MyUtil.flash(redirectAttributes, "success", "editSuccessful");
		request.logout();
		return "redirect:/";
	}
	
	/*Change password request*/
	@RequestMapping(value="/{userId}/change-password")
	public String changePassword(@PathVariable("userId") long userId, Model model){
		ChangePasswordForm form=new ChangePasswordForm();
		model.addAttribute(form);
		return "change-password";
	}
	
	@RequestMapping(value="/{userId}/change-password", method=RequestMethod.POST)
	public String changePassword(@PathVariable("userId") long userId,
			@ModelAttribute("changePasswordForm") @Valid ChangePasswordForm changePasswordForm,
			BindingResult result, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		
		userService.changePassword(userId, changePasswordForm, result);
		if(result.hasErrors()) return "change-password";	
		MyUtil.flash(redirectAttributes, "success", "editSuccessful");
		request.logout();
		return "redirect:/";
	}
	
	/*Change Email request*/
	@RequestMapping(value="/{userId}/change-email")
	public String changeEmail(@PathVariable("userId") long userId, Model model){
		ChangeEmailForm form=new ChangeEmailForm();
		model.addAttribute(form);
		return "change-email";
	}
	
	@RequestMapping(value="/{userId}/change-email", method=RequestMethod.POST)
	public String changeEmail(@PathVariable("userId") long userId,
			@ModelAttribute("changeEmailForm") @Valid ChangeEmailForm changeEmailForm,
			BindingResult result, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		
		userService.changeEmail(userId, changeEmailForm, result);
		if(result.hasErrors()) return "change-email";	
		MyUtil.flash(redirectAttributes, "success", "editSuccessful");
		request.logout();
		return "redirect:/";
	}
	
	/*Read a path variable from a url, Spring will create the request object for us*/
	@RequestMapping("/{changeEmailCode}/verifyEmail")
	public String verifyEmail(@PathVariable("changeEmailCode") String changeEmailCode,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		userService.verifyEmail(changeEmailCode);
		MyUtil.flash(redirectAttributes, "success", "verificationSuccess");
		request.logout();
		return "redirect:/";
	}
}
