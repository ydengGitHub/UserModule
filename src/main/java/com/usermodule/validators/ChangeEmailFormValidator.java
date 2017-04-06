package com.usermodule.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.usermodule.dto.ChangeEmailForm;
import com.usermodule.dto.SignupForm;
import com.usermodule.entities.User;
import com.usermodule.repositories.UserRepository;

@Component
public class ChangeEmailFormValidator extends LocalValidatorFactoryBean{
	private UserRepository userRepository;
	
	@Autowired
	public ChangeEmailFormValidator(UserRepository userRepository){
		this.userRepository=userRepository;
	}
	
	/*takes a class parameter, and indicate which class it is going to apply validation to*/
	@Override
	public boolean supports(Class<?> clazz){
		return clazz.isAssignableFrom(ChangeEmailForm.class);
	}
	
	/*Check the conditions we want, if there is an error, put in errors collection*/
	@Override
	public void validate(Object obj, Errors errors, final Object... validationHints){
		super.validate(obj, errors, validationHints);
		if(!errors.hasErrors()){
			ChangeEmailForm changeEmailForm=(ChangeEmailForm) obj;
			User user=userRepository.findByEmail(changeEmailForm.getEmail());
			/*Insert an error into errors collection, field, messageKey*/
			if(user != null) errors.rejectValue("email", "emailNotUnique");
		}
	}
}
