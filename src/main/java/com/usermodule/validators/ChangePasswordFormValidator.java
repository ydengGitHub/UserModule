package com.usermodule.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.usermodule.dto.ChangePasswordForm;
import com.usermodule.services.UserService;

@Component
public class ChangePasswordFormValidator extends LocalValidatorFactoryBean{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ChangePasswordForm.class);
	}

	@Override
	public void validate(Object obj, Errors errors, final Object... validationHints) {
		
		super.validate(obj, errors, validationHints);
		
		if (!errors.hasErrors()) {
			ChangePasswordForm changePasswordForm = (ChangePasswordForm) obj;
			if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getRetypePassword()))
				errors.reject("passwordsDoNotMatch");
		}
	}
}
