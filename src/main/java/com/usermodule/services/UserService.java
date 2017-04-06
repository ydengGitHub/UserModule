package com.usermodule.services;

import org.springframework.validation.BindingResult;

import com.usermodule.dto.ChangeEmailForm;
import com.usermodule.dto.ChangePasswordForm;
import com.usermodule.dto.ForgotPasswordForm;
import com.usermodule.dto.ResetPasswordForm;
import com.usermodule.dto.SignupForm;
import com.usermodule.dto.UserEditForm;
import com.usermodule.entities.User;

public interface UserService {
	public void signup(SignupForm signupForm);

	public void verify(String verificationCode);

	public void forgotPassword(ForgotPasswordForm forgotPasswordForm);

	public void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm, BindingResult result);

	public User findOne(long userId);

	public void update(long userId, UserEditForm userEditForm);
	
	public void changePassword(long userId,
			ChangePasswordForm changePasswordForm,
			BindingResult result);
	
	public boolean checkPassword(long userId, String password);

	public void changeEmail(long userId, ChangeEmailForm changeEmailForm, BindingResult result);

	public void verifyEmail(String changeEmailCode);
}
