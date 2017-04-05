package com.usermodule.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.usermodule.dto.ForgotPasswordForm;
import com.usermodule.entities.User;
import com.usermodule.repositories.UserRepository;

@Component
public class ForgotPasswordFormValidator extends LocalValidatorFactoryBean {
	
	private UserRepository userRepository;
	
	@Autowired
	public ForgotPasswordFormValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ForgotPasswordForm.class);
	}

	@Override
	public void validate(Object obj, Errors errors, final Object... validationHints) {
		
		super.validate(obj, errors, validationHints);
		
		if (!errors.hasErrors()) {
			ForgotPasswordForm forgotPasswordForm = (ForgotPasswordForm) obj;
			User user = userRepository.findByEmail(forgotPasswordForm.getEmail());
			if (user == null)
				errors.rejectValue("email", "notFound");		
		}
	}
}

